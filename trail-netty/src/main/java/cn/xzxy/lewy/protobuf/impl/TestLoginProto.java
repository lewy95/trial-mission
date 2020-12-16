//package cn.xzxy.lewy.protobuf.impl;
//
//import cn.xzxy.lewy.protobuf.pojo.LoginProto;
//
//public class TestLoginProto {
//
//    public static byte[] serialize() {
//        LoginProto.PlatForm sp = LoginProto.PlatForm.valueOf("IOS");
//        System.out.println("platform: " + sp.name()); // sp.name 和 sp.toString 一样
//        LoginProto.PlatForm[] platForms = LoginProto.PlatForm.values();
//        System.out.println("platForms size: " + platForms.length);
//
//        return null;
//    }
//
//    public static void main(String[] args) {
//        serialize();
//    }
//}
