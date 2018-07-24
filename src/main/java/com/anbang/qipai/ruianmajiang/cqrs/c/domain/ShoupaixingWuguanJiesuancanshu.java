package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.MajiangPai;
import com.dml.majiang.MajiangPlayer;

/**
 * 手牌型无关结算参数
 * 
 * @author Neo
 *
 */
public class ShoupaixingWuguanJiesuancanshu {

	private boolean pengchuHongzhong;
	private boolean gangchuHongzhong;
	private boolean pengchuFacai;
	private boolean gangchuFacai;
	private MajiangPai menFengPai;
	private boolean zuofengPeng;
	private boolean zuofengGang;
	private int baibanShu;
	private int caishenShu;
	private boolean allXushupaiInSameCategory;
	private boolean hasZipai;
	private boolean qingyise;
	private boolean hunyise;
	private int yijiupengShu;
	private int erbapengShu;
	private int fengzipengShu;
	private int yijiuminggangShu;
	private int erbaminggangShu;
	private int fengziminggangShu;
	private int fangruShoupaiCount;
	private int chichupaiZuCount;
	private int pengchupaiZuCount;
	private int gangchupaiZuCount;
	private MajiangPai[] yijiupaiArray;
	private MajiangPai[] erbapaiArray;
	private MajiangPai[] fengzipaiArray;

	public ShoupaixingWuguanJiesuancanshu(MajiangPlayer player) {
		pengchuHongzhong = player.ifPengchuForPaiType(MajiangPai.hongzhong);
		gangchuHongzhong = player.ifGangchuForPaiType(MajiangPai.hongzhong);
		pengchuFacai = player.ifPengchuForPaiType(MajiangPai.facai);
		gangchuFacai = player.ifGangchuForPaiType(MajiangPai.facai);
		menFengPai = player.fengpaiForMenfeng();
		zuofengPeng = player.ifPengchuForPaiType(menFengPai);
		zuofengGang = player.ifGangchuForPaiType(menFengPai);
		baibanShu = player.countPublicPai();
		caishenShu = player.countGuipai();
		allXushupaiInSameCategory = player.allXushupaiInSameCategory();
		hasZipai = player.hasZipai();
		qingyise = (allXushupaiInSameCategory && !hasZipai);
		hunyise = (allXushupaiInSameCategory && hasZipai);

		yijiupengShu = 0;
		yijiupaiArray = new MajiangPai[] { MajiangPai.yiwan, MajiangPai.jiuwan, MajiangPai.yitong, MajiangPai.jiutong,
				MajiangPai.yitiao, MajiangPai.jiutiao };
		for (int i = 0; i < yijiupaiArray.length; i++) {
			if (player.ifPengchuForPaiType(yijiupaiArray[i])) {
				yijiupengShu++;
			}
		}
		erbapengShu = 0;
		erbapaiArray = new MajiangPai[] { MajiangPai.erwan, MajiangPai.sanwan, MajiangPai.siwan, MajiangPai.wuwan,
				MajiangPai.liuwan, MajiangPai.qiwan, MajiangPai.bawan,

				MajiangPai.ertong, MajiangPai.santong, MajiangPai.sitong, MajiangPai.wutong, MajiangPai.liutong,
				MajiangPai.qitong, MajiangPai.batong,

				MajiangPai.ertiao, MajiangPai.santiao, MajiangPai.sitiao, MajiangPai.wutiao, MajiangPai.liutiao,
				MajiangPai.qitiao, MajiangPai.batiao };
		for (int i = 0; i < erbapaiArray.length; i++) {
			if (player.ifPengchuForPaiType(erbapaiArray[i])) {
				erbapengShu++;
			}
		}
		fengzipengShu = 0;
		fengzipaiArray = new MajiangPai[] { MajiangPai.dongfeng, MajiangPai.nanfeng, MajiangPai.xifeng,
				MajiangPai.beifeng, MajiangPai.hongzhong, MajiangPai.facai };
		for (int i = 0; i < fengzipaiArray.length; i++) {
			if (player.ifPengchuForPaiType(fengzipaiArray[i])) {
				fengzipengShu++;
			}
		}
		yijiuminggangShu = 0;
		for (int i = 0; i < yijiupaiArray.length; i++) {
			if (player.ifGangchuForPaiType(yijiupaiArray[i])) {
				yijiuminggangShu++;
			}
		}
		erbaminggangShu = 0;
		for (int i = 0; i < erbapaiArray.length; i++) {
			if (player.ifGangchuForPaiType(erbapaiArray[i])) {
				erbaminggangShu++;
			}
		}
		fengziminggangShu = 0;
		for (int i = 0; i < fengzipaiArray.length; i++) {
			if (player.ifGangchuForPaiType(fengzipaiArray[i])) {
				fengziminggangShu++;
			}
		}
		fangruShoupaiCount = player.countFangruShoupai();
		chichupaiZuCount = player.countChichupaiZu();
		pengchupaiZuCount = player.countPengchupaiZu();
		gangchupaiZuCount = player.countGangchupaiZu();
	}

	public boolean isPengchuHongzhong() {
		return pengchuHongzhong;
	}

	public boolean isGangchuHongzhong() {
		return gangchuHongzhong;
	}

	public boolean isPengchuFacai() {
		return pengchuFacai;
	}

	public boolean isGangchuFacai() {
		return gangchuFacai;
	}

	public MajiangPai getMenFengPai() {
		return menFengPai;
	}

	public boolean isZuofengPeng() {
		return zuofengPeng;
	}

	public boolean isZuofengGang() {
		return zuofengGang;
	}

	public int getBaibanShu() {
		return baibanShu;
	}

	public int getCaishenShu() {
		return caishenShu;
	}

	public boolean isAllXushupaiInSameCategory() {
		return allXushupaiInSameCategory;
	}

	public boolean isHasZipai() {
		return hasZipai;
	}

	public boolean isQingyise() {
		return qingyise;
	}

	public boolean isHunyise() {
		return hunyise;
	}

	public int getYijiupengShu() {
		return yijiupengShu;
	}

	public int getErbapengShu() {
		return erbapengShu;
	}

	public int getFengzipengShu() {
		return fengzipengShu;
	}

	public int getYijiuminggangShu() {
		return yijiuminggangShu;
	}

	public int getErbaminggangShu() {
		return erbaminggangShu;
	}

	public int getFengziminggangShu() {
		return fengziminggangShu;
	}

	public int getFangruShoupaiCount() {
		return fangruShoupaiCount;
	}

	public int getChichupaiZuCount() {
		return chichupaiZuCount;
	}

	public int getPengchupaiZuCount() {
		return pengchupaiZuCount;
	}

	public int getGangchupaiZuCount() {
		return gangchupaiZuCount;
	}

	public MajiangPai[] getYijiupaiArray() {
		return yijiupaiArray;
	}

	public MajiangPai[] getErbapaiArray() {
		return erbapaiArray;
	}

	public MajiangPai[] getFengzipaiArray() {
		return fengzipaiArray;
	}

}
