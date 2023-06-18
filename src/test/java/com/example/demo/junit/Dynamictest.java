package com.example.demo.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class Dynamictest {
	
	@TestFactory
    List<DynamicTest> dynamicTestsFromCollection() {
        return Arrays.asList(
            dynamicTest("1st dynamic test", () -> assertTrue(true)),
            dynamicTest("2nd dynamic test", () -> assertEquals(4, 2 * 2))
        );
    }
	
	@TestFactory
	Iterable<DynamicTest> dynamicTestFromIterable(){
		return Arrays.asList(
				dynamicTest("1st dynamic test", () -> assertTrue(true)),
	            dynamicTest("2nd dynamic test", () -> assertEquals(4, 2 * 2))
			);
	}
	
	@TestFactory
	Stream<DynamicTest> dynamicTestFromStream(){
		Stream<DynamicTest> test =  Stream.of("A","B","C")
				.map(str -> dynamicTest("Test" + " " + str, () -> System.out.println("haha")));
		return test;
	}
	
	@TestFactory
	Stream<DynamicTest> dynamicTestFromIntStream(){
		return IntStream.iterate(1, n-> n*3).limit(10)
				.mapToObj(n -> dynamicTest("Test" + " " + n,() -> System.out.println(n)));			
	}
	
	@TestFactory
	Iterator<DynamicTest> dynamaicTestFromIterator(){
		return Arrays.asList(
				dynamicTest("1st dynamic test", () -> assertTrue(true)),
	            dynamicTest("2nd dynamic test", () -> assertEquals(4, 2 * 2))
			).iterator();
	}
	
}
