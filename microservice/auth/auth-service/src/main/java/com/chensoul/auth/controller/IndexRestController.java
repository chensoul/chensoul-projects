package com.chensoul.auth.controller;

import com.chensoul.collection.MapUtils;
import com.chensoul.util.R;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Index Controller
 */
@RestController
@RequestMapping
public class IndexRestController {

    /**
     * index
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping()
    public R<Map> index(HttpServletRequest request, HttpServletResponse response) {
        return R.ok(MapUtils.of());
    }
}
