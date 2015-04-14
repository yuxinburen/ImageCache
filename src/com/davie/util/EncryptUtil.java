package com.davie.util;

/**
 * User: davie
 * Date: 15-4-14
 */

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * 常见的加密算法工具类
 */
public class EncryptUtil {


    //消息摘要////////////////

    //MD5

    /**
     * MD5 消息摘要,对输入的内容,进行唯一标识的计算.
     * @param data
     * @return
     */
    public static byte[] md5(byte[] data){
        byte [] ret = null;
        if (data!=null){
            try {
                //创建消息摘要对象
                MessageDigest digest = MessageDigest.getInstance("MD5");

                ret = digest.digest(data);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


    /**
     *SHA1消息摘要,对输入的内容,进行唯一标识的计算.
     * @param data
     * @return
     */
    public static byte[] sha1(byte[] data){
        byte [] ret = null;
        if (data!=null){
            try {
                //创建消息摘要对象
                MessageDigest digest = MessageDigest.getInstance("SHA-1");

                ret = digest.digest(data);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /////RSA 非对称加密算法

    /**
     *RSA 加密算法,加密的部分,支持PrivateKey加密,同时也支持PublicKey加密
     *
     * 因为 PrivateKey 实现了 Key接口,同时PublicKey也实现了 Key接口
     * Cipher.init(int,key)
     * @param data
     * @param key
     * @return
     */
    public static byte[] rsaEncrypt(byte [] data,Key key){
        byte[] ret = null;
        if (data != null && key!=null) {
            if(key instanceof PublicKey|| key instanceof  PrivateKey){
                try {

                    Cipher cipher = Cipher.getInstance("RSA");
                    //初始化操作
                    cipher.init(Cipher.ENCRYPT_MODE,key);

                    ret = cipher.doFinal(data);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    ///////////////////

    /**
     *RSA 解密算法,加密的部分,支持PrivateKey加密,同时也支持PublicKey加密
     *
     * 因为 PrivateKey 实现了 Key接口,同时PublicKey也实现了 Key接口
     * Cipher.init(int,key)
     * @param data
     * @param key
     * @return
     */
    public static byte[] rsaDecrypt(byte [] data,Key key){
        byte[] ret = null;
        if (data != null && key!=null) {
            if(key instanceof PublicKey|| key instanceof  PrivateKey){
                try {

                    Cipher cipher = Cipher.getInstance("RSA");
                    //初始化操作
                    cipher.init(Cipher.DECRYPT_MODE,key);

                    ret = cipher.doFinal(data);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    ///////////////////
















    /////// AES 算法   /////

    /**
     * 采用Cipher.getInstance("AES");方式,并且直接指定密码的加密
     * @param data 数据
     * @param pwd 密码 密码长度建议16个字节
     * @return
     */
    public static byte[] aesEncrypt(byte [] data,byte [] pwd){
        byte [] ret = null;
        if (data != null && pwd!=null) {
            //检查密码长度,进行补充操作
            int plen = pwd.length;
            if(plen<16){//128bit
                byte [] fully = new byte[16];
                System.arraycopy(pwd,0,fully,0,plen);
                pwd = fully;
            }

            try {
                ///!!! AES 的加密,算法部分是支持多种形式的
                //  如果 写的是 AES,那么对应的解密方法,使用的算法必须是 AES
                ///!!! AES 的加密,还可以使用 AES/CBC/PKCS5Padding 这个算法
                //     对应的解密算法必须使用"AES/CBC/PKCS5Padding"才可以解密
                ///!!! 加密的算法部分与解密的算法部分必须一致,才可以正确解码
                Cipher cipher = Cipher.getInstance("AES");

                //AES 算法密码部分使用
                //    new SecretKeySpec(byte [] pwd,String alg)
                SecretKeySpec key = new SecretKeySpec(pwd,"AES");

                cipher.init(Cipher.ENCRYPT_MODE,key);

                ret = cipher.doFinal(data);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }


        }
        return ret;
    }

    /**
     * AES解密,使用 Cipher.getInstance("AES")方式的
     * @param data 解密的数据
     * @param pwd
     * @return
     */
    public static byte[] aesDecrypt(byte [] data,byte [] pwd){
        byte [] ret = null;
        if (data != null && pwd!=null) {
            //检查密码长度,进行补充操作
            int plen = pwd.length;
            if(plen<16){//128bit
                byte [] fully = new byte[16];
                System.arraycopy(pwd,0,fully,0,plen);
                pwd = fully;
            }

            try {
                ///!!! AES 的加密,算法部分是支持多种形式的
                //  如果 写的是 AES,那么对应的解密方法,使用的算法必须是 AES
                ///!!! AES 的加密,还可以使用 AES/CBC/PKCS5Padding 这个算法
                //     对应的解密算法必须使用"AES/CBC/PKCS5Padding"才可以解密
                ///!!! 加密的算法部分与解密的算法部分必须一致,才可以正确解码
                Cipher cipher = Cipher.getInstance("AES");

                //AES 算法密码部分使用
                //    new SecretKeySpec(byte [] pwd,String alg)
                SecretKeySpec key = new SecretKeySpec(pwd,"AES");

                cipher.init(Cipher.DECRYPT_MODE,key);

                ret = cipher.doFinal(data);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }


        }
        return ret;
    }

    ///////////////////////


    //自定义填充方式 AES 加密
    //使加密的内容,强度更高

    /**
     * 使用密码和Iv两个参数来进行加密,我们可以理解为用两个密码进行加密
     * Iv可以看做另外一个密码,对于解密而言,密码和Iv同样要一致
     * @param data
     * @param pwd
     * @return
     */
    public static byte [] aesIvEncrypt(byte [] data,byte [] pwd,byte [] ivData){
        byte [] ret = null;
        if (data != null && pwd!=null && ivData!=null ) {
            int plen = pwd.length;
            if(plen<16){
                byte [] fully = new byte[16];
                System.arraycopy(pwd,0,fully,0,plen);
                pwd = fully;
            }

            plen = ivData.length;
            if(plen<16){
                byte [] fully = new byte[16];
                System.arraycopy(ivData,0,fully,0,plen);
                ivData = fully;
            }

            try {
                //创建cipher
                //AES/CBC/PKCS5Padding
                //AES/ECB/PKCS5Padding
                //AES/CBC/NoPadding
                //AES/ECB/NoPadding
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

                SecretKeySpec key = new SecretKeySpec(pwd,"AES");

                //使用Iv增加AES的加密强度,参数必须是16字节
                IvParameterSpec iv = new IvParameterSpec(ivData);
                //初始化：使用密码和Iv进行初始化
                //Iv 就是 AES中,另一套密码
                cipher.init(Cipher.ENCRYPT_MODE,key,iv);

                ret = cipher.doFinal(data);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


    /////////////////////////////////////////


    /**
     * 使用密码和Iv两个参数来进行加密,我们可以理解为用两个密码进行加密
     * Iv可以看做另外一个密码,对于解密而言,密码和Iv同样要一致
     * @param data
     * @param pwd
     * @return
     */
    public static byte [] aesIvDecrypt(byte [] data,byte [] pwd,byte [] ivData){
        byte [] ret = null;
        if (data != null && pwd!=null && ivData!=null ) {
            int plen = pwd.length;
            if(plen<16){
                byte [] fully = new byte[16];
                System.arraycopy(pwd,0,fully,0,plen);
                pwd = fully;
            }

            plen = ivData.length;
            if(plen<16){
                byte [] fully = new byte[16];
                System.arraycopy(ivData,0,fully,0,plen);
                ivData = fully;
            }

            try {
                //创建cipher
                //AES/CBC/PKCS5Padding
                //AES/ECB/PKCS5Padding
                //AES/CBC/NoPadding
                //AES/ECB/NoPadding
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

                SecretKeySpec key = new SecretKeySpec(pwd,"AES");

                //使用Iv增加AES的加密强度,参数必须是16字节
                IvParameterSpec iv = new IvParameterSpec(ivData);
                //初始化：使用密码和Iv进行初始化
                //Iv 就是 AES中,另一套密码
                cipher.init(Cipher.DECRYPT_MODE,key,iv);

                ret = cipher.doFinal(data);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * DES解密
     *
     * @param enData
     * @param pwd
     * @return
     */
    public static byte[] desDecrypt(byte[] enData, byte[] pwd) {
        byte[] ret = null;
        if (enData != null && pwd != null && pwd.length == 8) {
            try {
                Cipher cipher = Cipher.getInstance("DES");
                //初始化,通过初始化可以控制具体的执行操作,是加密还是解密
                //ENCRYPT_MODE加密模式     DECRYPT_MODE解密模式
                //加密模式  加密需要密码,而且是DES 8个字节的密码,这是DES加密算法的要求
                //第二个参数是Key类型,是java系统中的特定类
                //对于DES加密来说,Key采用SecretKeyFactory来生成.

                //加密API所有的工具,都需要通过getInstance(具体的加密算法)来获取
                SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
                //该方法,生成实际的Key对象,参数就是密码的参数,就是实际的密码数据信息
                //对于DES来说,就是DESKeySpec
                SecretKey key = secretKeyFactory.generateSecret(new DESKeySpec(pwd));

                //代表当前是解密状态
                cipher.init(Cipher.DECRYPT_MODE, key);
                ret = cipher.doFinal(enData);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * DESede解密
     *
     * @param enData
     * @param pwd
     * @return
     */
    public static byte[] desedeDecrypt(byte[] enData, byte[] pwd) {
        byte[] ret = null;
        if (enData != null && pwd != null && pwd.length == 24) {
            try {
                Cipher cipher = Cipher.getInstance("DESede");
                //初始化,通过初始化可以控制具体的执行操作,是加密还是解密
                //ENCRYPT_MODE加密模式     DECRYPT_MODE解密模式
                //加密模式  加密需要密码,而且是DES 8个字节的密码,这是DES加密算法的要求
                //第二个参数是Key类型,是java系统中的特定类
                //对于DES加密来说,Key采用SecretKeyFactory来生成.

                //加密API所有的工具,都需要通过getInstance(具体的加密算法)来获取
                SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
                //该方法,生成实际的Key对象,参数就是密码的参数,就是实际的密码数据信息
                //对于DES来说,就是DESKeySpec
                SecretKey key = secretKeyFactory.generateSecret(new DESedeKeySpec(pwd));

                //代表当前是解密状态
                cipher.init(Cipher.DECRYPT_MODE, key);

                ret = cipher.doFinal(enData);

                return ret;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * DESede(3DES)加密算法
     *
     * @param data 原始数据
     * @param pwd  密码,密码需要24个字节
     * @return byte[] 加密后的数据
     */
    public static byte[] desedeEncrypt(byte[] data, byte[] pwd) {
        byte[] ret = null;
        if (data != null && pwd != null) {
            int plen = pwd.length;
            //确保密码是24个字节
            if (plen < 24) {
                byte[] fully = new byte[24];
                System.arraycopy(pwd, 0, fully, 0, plen);
                pwd = fully;
            }
            try {

                Cipher cipher = Cipher.getInstance("DESede");

                SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");

                SecretKey key = secretKeyFactory.generateSecret(new DESedeKeySpec(pwd));

                cipher.init(Cipher.ENCRYPT_MODE, key);

                ret = cipher.doFinal(data);
                return ret;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static String toHex(byte[] data) {
        String ret = null;
        if (data != null) {
            int len = data.length;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                int iv = data[i];
                int ih = (iv >> 4) & 0x0F;
                int il = iv & 0x0F;

                char ch, cl;
                if (ih > 9) {
                    ch = (char) ('A' + (ih - 10));
                } else {
                    ch = (char) ('0' + ih);
                }

                if (il > 9) {
                    cl = (char) ('A' + (il - 10));
                } else {
                    cl = (char) ('0' + il);
                }

                sb.append(ch).append(cl);
            }
            ret = sb.toString();
        }
        return ret;
    }

    /**
     * 将十六进制表示的字符串,还原为字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] fromHex(String hex) {
        byte[] ret = null;
        if (hex != null) {
            //hex 长度永远为偶数
            if (hex.length() % 2 == 0) {
                ret = new byte[hex.length() >> 1];

                int len = hex.length() - 1;
                for (int i = 0; i < len; i += 2) {
                    char ch = hex.charAt(i);
                    char cl = hex.charAt(i + 1);
                    int ih, il;
                    if (ch >= 'A') {
                        ih = ch - 'A' + 10;
                    } else {
                        ih = ch - '0';
                    }

                    if (cl >= 'A') {
                        il = cl - 'A' + 10;
                    } else {
                        il = cl - '0';
                    }

                    int iv = ((ih & 0x0F) << 4) | (il & 0x0F);
                    byte b = (byte) iv;

                }
            }
        }
        return ret;
    }
}
