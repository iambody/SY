package app.product.com.model;

import com.cgbsoft.lib.base.model.bean.BaseBean;

import java.util.List;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/9-11:18
 */
public class ProductlsBean   {
    //产品ID
    public String productId;
    //产品类型
    public String productType;
    //产品名称
    public String productName;
    //起投金额
    public int buyStart;
    //累计净值
    public String cumulativeNet;
    //收益基准
    public double expectedYield;
    //资产标签
    public String label;
    //开户行
    public String raiseAccountName;
    //募集账号
    public String raiseAccount;
    //募集银行
    public String raiseBank;
    //产品系列
    public String series;
    //剩余额度
    public int remainingAmount;
    public String remainingAmountStr;
    //产品状态  50: 正常 60: 暂停募集 70: 已清算  80: 分销方未上线
    public String state;
    //截止认购时间
    public String raiseEndTime;
    //是否热门产品
    public String isHotProduct;
    //营销图片地址
    public String marketingImageUrl;
    //营销话术
    public String hotName;
    //营销短信
    public String smsContent;
    //产品的发行方案
    public List<Schemes> schemes;
    //产品的投资单元
    public List<Units> units;
    //发行方案ID
    public String schemeId;

    public String term;

    public String shortName;

    public String incomeMax;

    public String netAll;

    public String investmentArea;

    //    产品的发行期
    public static class Schemes {

        public String id;//":"34b08935e59d41ceb44eeb1f1c3cff19",
        public String state;//":"60",
        public String schemeName;//":"份额转让（转让单位价格：1.0188）",
        public String productId;//":"4c67c13bc9804e7390fb8841ddcb0eb6"
    }

    //产品投资单元units
    public  static class Units {
        public String id;//":"f1a9339d3f8149d4b007c84c63a4b76d",
        public String name;//":"A类A类",
        public String productId;//":"4c67c13bc9804e7390fb8841ddcb0eb6"
    }

}
