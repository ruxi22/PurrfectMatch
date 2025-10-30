package com.wad.firstmvc.controllers;

import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;

public class AdminControllerTest {

    private final AdminController controller = new AdminController();

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
