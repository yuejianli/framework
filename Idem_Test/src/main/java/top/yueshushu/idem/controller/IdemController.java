package top.yueshushu.idem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.idem.annotation.IdemInspect;
import top.yueshushu.idem.core.chain.DedupChainPolicy;
import top.yueshushu.idem.core.chain.IdemChainPolicy;
import top.yueshushu.idem.vo.UserRequestVO;

/**
 * Controller 验证信息
 *
 * @author yuejianli
 * @date 2022-11-03
 */
@RestController
@Slf4j
@RequestMapping("/idem")
public class IdemController {

    /**
     * 不配置 Idem 的操作
     */
    @PostMapping("/noIdem")
    public UserRequestVO noIdem(@RequestBody UserRequestVO userRequestVO) {
        log.info(">>>>> 执行添加操作 :{}", userRequestVO);
        return userRequestVO;
    }

    /**
     * 配置 Idem 幂等的操作
     */
    @PostMapping("/idem1")
    @IdemInspect(policy = IdemChainPolicy.class)
    public UserRequestVO idem1(@RequestBody UserRequestVO userRequestVO) {
        log.info(">>>>> 执行添加操作 :{}", userRequestVO);
        return userRequestVO;
    }

    /**
     * 配置 Idem 幂等的操作
     */
    @PostMapping("/idem2")
    @IdemInspect(policy = IdemChainPolicy.class, waitTimeSeconds = 3, expiredTimeSeconds = 5, methodParamsName = {"id", "name"})
    public UserRequestVO idem2(@RequestBody UserRequestVO userRequestVO) {
        log.info(">>>>> 执行添加操作 :{}", userRequestVO);
        return userRequestVO;
    }


    /**
     * 配置 Idem 幂等的操作
     */
    @PostMapping("/idem3")
    @IdemInspect(policy = DedupChainPolicy.class, waitTimeSeconds = 3, expiredTimeSeconds = 5, methodParamsName = {"id", "name"})
    public UserRequestVO idem3(@RequestBody UserRequestVO userRequestVO) {
        log.info(">>>>> 执行添加操作 :{}", userRequestVO);
        return userRequestVO;
    }
}
