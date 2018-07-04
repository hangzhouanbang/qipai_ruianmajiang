package com.anbang.qipai.ruianmajiang.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.ruianmajiang.cqrs.c.service.PlayerAuthService;
import com.anbang.qipai.ruianmajiang.cqrs.q.service.MajiangPlayQueryService;
import com.anbang.qipai.ruianmajiang.web.vo.CommonVO;
import com.dml.majiang.PanValueObject;

/**
 * 打麻将相关
 * 
 * @author neo
 *
 */
@RestController
@RequestMapping("/mj")
public class MajiangController {

	@Autowired
	private MajiangPlayQueryService majiangPlayQueryService;

	@Autowired
	private PlayerAuthService playerAuthService;

	/**
	 * 当前盘我应该看到的所有信息
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/pan_for_me")
	@ResponseBody
	public CommonVO panforme(String token, String gameId) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		PanValueObject panValueObject;
		try {
			panValueObject = majiangPlayQueryService.findAndFilterCurrentPanValueObjectForPlayer(gameId, playerId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getMessage());
			return vo;
		}
		data.put("pan", panValueObject);
		return vo;
	}

}
