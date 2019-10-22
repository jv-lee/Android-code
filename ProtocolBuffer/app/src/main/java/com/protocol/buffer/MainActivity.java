package com.protocol.buffer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.protobuf.InvalidProtocolBufferException;
import com.protocol.model.AddressProto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //创建数据对象
        AddressProto.Person.Builder builder = AddressProto.Person.newBuilder();
        AddressProto.Person person = builder.setEmail("xxx").build();

        //byte数组 序列化
        byte[] bytes = person.toByteArray();
        try {
            //byte数组 反序列化
            AddressProto.Person parsePerson = AddressProto.Person.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        try {
            //流 序列化
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            person.writeTo(byteArrayOutputStream);
            byte[] streamBytes = byteArrayOutputStream.toByteArray();

            //流 反序列化
            AddressProto.Person streamPerson = AddressProto.Person.parseFrom(new ByteArrayInputStream(streamBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
