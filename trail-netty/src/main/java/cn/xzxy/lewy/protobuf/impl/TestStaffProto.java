package cn.xzxy.lewy.protobuf.impl;

import cn.xzxy.lewy.protobuf.pojo.StaffProto;
import com.google.protobuf.InvalidProtocolBufferException;

public class TestStaffProto {

    /**
     * 序列化
     */
    public static byte[] serialize() {
        // 错误的创建方式：
        // 私有构造函数 private LoginProto() {}， 创建失败
        // LoginProto loginProto = new LoginProto();

        // 正确的创建方式：
        // 1. 创建 Builder
        StaffProto.Staff.Builder builder = StaffProto.Staff.newBuilder();
        // 2. 设置属性
        builder.setAge(30);
        builder.setName("干饭人");
        // 3. 通过builder创建实体对象
        StaffProto.Staff staff = builder.build();
        // 4. 序列化，可以将序列化后的bytes保存在本地或者是传到网络
        return staff.toByteArray();
    }

    /**
     * 反序列化
     */
    public static void antiSerialize(byte[] data) {
        try {
            // 反序列化解析，data可以是本地数据或者是网络数据
            StaffProto.Staff antiStaff = StaffProto.Staff.parseFrom(data);
            System.out.println(antiStaff.getAge());
            System.out.println(antiStaff.getName());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        byte[] data = serialize();
        antiSerialize(data);
    }
}
