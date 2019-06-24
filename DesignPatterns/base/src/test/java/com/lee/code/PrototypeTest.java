package com.lee.code;

import com.lee.code.build.prototype.Citation;
import com.lee.code.build.prototype.Realizetype;
import com.lee.code.build.prototype.manager.ProtoTypeManager;
import com.lee.code.build.prototype.manager.Shape;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description
 */
public class PrototypeTest {

    @Test
    public void test() throws CloneNotSupportedException {
        //原型模式
        Realizetype obj1 = new Realizetype();
        Realizetype obj2 = (Realizetype) obj1.clone();
        System.out.println("obj1==obj2?" + (obj1 == obj2));

        //原型模式返回相似对象
        Citation citation1 = new Citation("张三", "同学：在1092学年第一学期表现优秀，被评为三好学生。", "非酋学院");
        citation1.display();
        Citation citation2 = (Citation) citation1.clone();
        citation2.setName("李四");
        citation2.display();

        //通过原型管理器 返回原型
        ProtoTypeManager protoTypeManager = new ProtoTypeManager();
        Shape shape1 = protoTypeManager.getShape("Circle");
        shape1.countArea(30);

        Shape square2 = protoTypeManager.getShape("Square");
        square2.countArea(50);
    }
}
