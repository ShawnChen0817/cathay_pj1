package com.example.demo;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ArticleTest.class,LoginTest.class,MessageTest.class,TestSuccess.class})
public class TestAll {

}
