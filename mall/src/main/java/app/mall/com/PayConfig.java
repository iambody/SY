package app.mall.com;


/**
 * 支付配置
 */
public class PayConfig {

    /**
     * 应用编号：
     * 应用在iAppPay云支付平台的编号，此编号用于应用与iAppPay云支付平台的sdk集成
     */
    public static final String appid = "3012397237";

    /**
     * 应用名称：
     * 应用在iAppPay云支付平台注册的名称
     */
    public final static String APP_NAME = "私募云";

    /**
     * 商品编号：
     * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
     * 编号对应商品名称为：衡阳牛肉
     */
    public final static int WARES_ID_1 = 1;

    /**
     * 商品编号：
     * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
     * 编号对应商品名称为：100云豆
     */
    public final static int WARES_ID_2 = 2;

    /**
     * 应用私钥：
     * 用于对商户应用发送到平台的数据进行加密
     */
    public final static String privateKey = "MIICXgIBAAKBgQDlna3KW+ylcbJIlalO7CefIK6MLkikSdV/atQbZ+q6X0jTnILrbMurDtM/kuzYccPMI92tQXlt/Axl0Zc4FoxWWiglt5Fj5popkJXUQuGJnkvaag8/5YDTkR/uOFYL/SDu6k8J7e67jwoy4Vnyl4AOkJAGIGgrh9tD0osHYZ92YQIDAQABAoGAA7B142N4uOvLvojREJiQxmjDBTOVLTaWfJad09bmmZoAVI4WOGwmFZiGYmYc8hlZ2QTWgfskVVUQae5lLJyIIq9DmLYvJoxfrtYV1QyEOwWcU+Jc5YtRAOlVcrl0JQO4PHKZ/g+FEmyhcF0fxXY9iVadycY4H5idDvAtedWgd8ECQQDz4eVEbXpSO7KeXDFiHREX8hwm54OPNOVmTkCnR2CJcbDn+PNKCcYO7MA2JSOMzpV7vSxM4CCRSdyt22UtfpN5AkEA8QZRhWhK8PCGj9gdUrhtYU/l9x0PrHgfMcgurzxC8gsZVpcVDpbuw/oyl7loM4l2IHZfS2l0ifrH2H2MipqYKQJBAOq7U5XG4WLgvoyZI5bRbzBjASGY1xEw0lLjomre0lW3rZO4E0IBojWfPWlf2ZWfCFVuwILFfdMZCmPztLBTEdECQQCFTiBSb89bGTzjW/1D3hspAj7HgPxFQJ7IMtaXNvYz1q2p9Z/A1PuzyrKmtfYK4xFBfRbRp767/ccAVtgfZMeZAkEAlERr5Pfspy+g/QVlMdyoDJtFAg8GCbzoxGzK3TdslQWF2ylxpO6C0y+fxaxmTJzZxjE0XbuPbb0xc8bfIkImIQ==";

}
