package com.chensoul.authserver.controller;

import com.chensoul.collection.MapUtils;
import com.chensoul.util.R;
import java.security.Principal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Index Controller
 */
@Slf4j
@RestController
@RequestMapping
public class IndexRestController {

    /**
     * index
     *
     * @return
     */
    @GetMapping()
    public R<Map> index(Principal principal) {
        log.info("principal: {}", principal);

        return R.ok(MapUtils.of("user", principal));
    }
}
