package com.gionee.gnservice.sdk.member.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.entity.MemberPrivilege;
import com.gionee.gnservice.entity.MemberPrivilegeContent;
import com.gionee.gnservice.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by caocong on 11/25/16.
 */
public class DatabaseManager implements IMemberPrivilegeDatabase {
    private static final String TAG = "DatabaseManager";
    private DatabaseHelper mDBHelper;

    public DatabaseManager(Context context) {
        mDBHelper = new DatabaseHelper(context);
    }

    @Override
    public boolean saveMemberPrivileges(List<MemberPrivilege> memberPrivileges) {
        if (memberPrivileges == null) {
            return false;
        }
        MemberLevel memberLevel = null;
        List<MemberPrivilegeContent> contents = new ArrayList<>();
        for (MemberPrivilege memberPrivilege : memberPrivileges) {
            if (memberLevel == null) {
                memberLevel = memberPrivilege.getMemberLevel();
            }
            contents.addAll(memberPrivilege.getContentParts());
        }
        clearOverduePrivilege(memberLevel.getValue());
        return insertPrivilege(memberPrivileges) && insertPrivilegeContents(contents);
    }

    private void clearOverduePrivilege(int memberLevel) {
        LogUtil.i(TAG, "clearOverduePrivilege() memberLevel = " + memberLevel);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String selection = Columns.CateColumns.MEMBER_LEVEL + "=?";
            Cursor cursor = db.query(Columns.CATE_TABLE, null, selection, new String[]{String.valueOf(memberLevel)}, null, null, null);
            cursor.moveToFirst();
            String cidSelection = Columns.ContentColumns.CID + "=?";
            while (!cursor.isAfterLast()) {
                String contentId = cursor.getString(cursor.getColumnIndexOrThrow(Columns.CateColumns.ID));
                db.delete(Columns.CONTENT_TABLE, cidSelection, new String[]{contentId});
                cursor.moveToNext();
            }
            db.delete(Columns.CATE_TABLE, selection, new String[]{String.valueOf(memberLevel)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean insertPrivilege(List<MemberPrivilege> memberPrivileges) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            for (MemberPrivilege mp : memberPrivileges) {
                db.insert(Columns.CATE_TABLE, null, buildCategoryValues(mp));
                //insertPrivilegeContents(mp.getContentParts());
            }
            db.setTransactionSuccessful();
            LogUtil.d(TAG, "insert cates infos to db successful!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(TAG, "insert cates infos to db fail!" + e.getMessage());
            return false;
        } finally {
            db.endTransaction();
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean insertPrivilegeContents(List<MemberPrivilegeContent> contents) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            for (MemberPrivilegeContent mpc : contents) {
                db.insert(Columns.CONTENT_TABLE, null, buildContentValues(mpc));
            }
            db.setTransactionSuccessful();
            LogUtil.d(TAG, "insert content infos to db successful!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(TAG, "insert content infos to db fail!");
            return false;
        } finally {
            db.endTransaction();
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ContentValues buildCategoryValues(MemberPrivilege mp) {
        ContentValues values = new ContentValues();
        values.put(Columns.CateColumns.ID, mp.getId());
        values.put(Columns.CateColumns.NAME, mp.getName());
        values.put(Columns.CateColumns.DESC, mp.getDescription());
        values.put(Columns.CateColumns.ICON, mp.getIconUrl());
        values.put(Columns.CateColumns.ICON_2, mp.getIcon2Url());
        values.put(Columns.CateColumns.IMG, mp.getImgUrl());
        values.put(Columns.CateColumns.MEMBER_LEVEL, String.valueOf(mp.getMemberLevel().getValue()));
        return values;
    }

    private ContentValues buildContentValues(MemberPrivilegeContent mpc) {
        ContentValues values = new ContentValues();
        values.put(Columns.ContentColumns.ID, mpc.getId());
        values.put(Columns.ContentColumns.NAME, mpc.getName());
        values.put(Columns.ContentColumns.CONTENT, mpc.getContent());
        values.put(Columns.ContentColumns.CID, mpc.getCid());
        return values;
    }

    @Override
    public synchronized List<MemberPrivilege> queryMemberPrivilegesByMemberLevel(MemberLevel memberLevel) {
        LogUtil.d(TAG, "query member privilege by member level:" + memberLevel.getValue());
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        if (db == null) {
            return null;
        }
        List<MemberPrivilege> privileges = new ArrayList<MemberPrivilege>();
        String selection = Columns.CateColumns.MEMBER_LEVEL + "=?";
        Cursor cursor = db.query(Columns.CATE_TABLE, null, selection, new String[]{String.valueOf(memberLevel.getValue())}, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                MemberPrivilege privilege = new MemberPrivilege();
                privilege.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Columns.CateColumns.ID)));
                privilege.setName(cursor.getString(cursor.getColumnIndexOrThrow(Columns.CateColumns.NAME)));
                privilege.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Columns.CateColumns.DESC)));
                privilege.setIconUrl(cursor.getString(cursor.getColumnIndexOrThrow(Columns.CateColumns.ICON)));
                privilege.setIcon2Url(cursor.getString(cursor.getColumnIndexOrThrow(Columns.CateColumns.ICON_2)));
                privilege.setImgUrl(cursor.getString(cursor.getColumnIndexOrThrow(Columns.CateColumns.IMG)));
                String level = cursor.getString(cursor.getColumnIndexOrThrow(Columns.CateColumns.MEMBER_LEVEL));
                privilege.setMemberLevel(MemberLevel.get(Integer.valueOf(level)));
                String id = cursor.getString(cursor.getColumnIndexOrThrow(Columns.CateColumns.ID));
                List<MemberPrivilegeContent> contents = queryMemberPrivilegeContensById(id);
                privilege.setContentParts(contents);
                privileges.add(privilege);
                cursor.moveToNext();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return privileges;
    }


    private List<MemberPrivilegeContent> queryMemberPrivilegeContensById(String id) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        if (db == null) {
            return null;
        }
        List<MemberPrivilegeContent> mpcs = new ArrayList<MemberPrivilegeContent>();
        String selection = Columns.ContentColumns.CID + "=?";
        Cursor cursor = db.query(Columns.CONTENT_TABLE, null, selection, new String[]{id}, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                MemberPrivilegeContent mpc = new MemberPrivilegeContent();
                mpc.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Columns.ContentColumns.ID)));
                mpc.setName(cursor.getString(cursor.getColumnIndexOrThrow(Columns.ContentColumns.NAME)));
                mpc.setContent(cursor.getString(cursor.getColumnIndexOrThrow(Columns.ContentColumns.CONTENT)));
                mpc.setCid(cursor.getInt(cursor.getColumnIndexOrThrow(Columns.ContentColumns.CID)));
                mpcs.add(mpc);
                cursor.moveToNext();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mpcs;
    }

    @Override
    public int queryVersion() {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        if (db == null) {
            return 0;
        }
        int value = 0;
        Cursor cursor = db.query(Columns.VERSION_TABLE, null, null, null, null, null, null);
        try {
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    value = cursor.getInt(0);
                    LogUtil.d(TAG, "get version=" + value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    @Override
    public boolean saveVersion(String version) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int delete = 0;
        long insert = 0;
        try {
            if (db == null) {
                return false;
            }
            delete = db.delete(Columns.VERSION_TABLE, null, null);
            ContentValues values = new ContentValues();
            values.put(Columns.VersionColumns.VERSION, version);
            insert = db.insert(Columns.VERSION_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        LogUtil.d(TAG, "save version result is:" + (delete > 0 && insert > -1));
        return delete > 0 && insert > -1;
    }

    @Override
    public long queryLastUpdateTime() {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        if (db == null) {
            return 0;
        }
        long value = 0;
        Cursor cursor = db.query(Columns.UPDATE_TIME_TABLE, null, null, null, null, null, null);
        try {
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    value = cursor.getLong(0);
                    LogUtil.d(TAG, "get last time is=" + value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    @Override
    public boolean saveLastUpdateTime(long updateTime) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (db == null) {
            return false;
        }
        int delete = 0;
        long insert = 0;
        try {
            delete = db.delete(Columns.UPDATE_TIME_TABLE, null, null);
            ContentValues values = new ContentValues();
            values.put(Columns.UpdateTimeColumns.UPDATE_TIME, updateTime);
            insert = db.insert(Columns.UPDATE_TIME_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LogUtil.d(TAG, "save last update time result is:" + (delete > 0 && insert > -1));
        return delete > 0 && insert > -1;
    }

    @Override
    public boolean deleteAllDatas() {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int delete1 = 0;
        int delete2 = 0;
        try {
            if (db == null) {
                return false;
            }
            delete1 = db.delete(Columns.CATE_TABLE, null, null);
            delete2 = db.delete(Columns.CONTENT_TABLE, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (db != null) {
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LogUtil.d(TAG, "delete all database data result is:" + (delete1 > 0 && delete2 > 0));
        return delete1 > 0 && delete2 > 0;
    }

}
