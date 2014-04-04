### Bean Validation [Bean Validation 技术规范特性概述](http://www.ibm.com/developerworks/cn/java/j-lo-beanvalid/)

javax.validation.constraints包下提供一些基础的约束注解,一些Bean Validation都实现了这些基础约束注解的检验

javax.validation.constraints包下包括`@NotNull` `@Null` `@Size` `@Min` `@Max` `@Digits`等等

通过`javax.validation.Validation`可以加载类路径下的具体Bean Validation实现

### 用户自定义约束注解

必须包含三个主要方法`String message()` `Class<?> group()` `Class<? extends Payload> payload()`

`@Constraint`用于指定检验的执行者,检验者均实现`javax.validation.ConstraintValidator`

ZipCode.java

	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Constraint(validatedBy = { ZipCodeValidator.class })
	public @interface ZipCode {
	
	    String message() default "zip code may not be right";
	
	    Class<?>[] groups() default {};
	
	    Class<? extends Payload>[] payload() default {};
	    
	}

### 用户自定义约束验证类

自定义类实现了`javax.validation.ConstraintValidator`中的`initialize` `isValid`两个方法,两方法用于初始化以及实际验证逻辑

	public class ZipCodeValidator implements ConstraintValidator<ZipCode, String> {

		private int length;
		private boolean nullable;
		
		@Override
		public void initialize(ZipCode constraint) {
			length = constraint.length();
			nullable = constraint.nullable();
		}
	
		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			if (nullable && value == null)
				return true;
			return value.length() == length;
		}
	}

Foo.java

Foo中zipCode使用`@ZipCode`表明该值需要检验

	public class Foo {
		@NotNull(message = "foo.name can't be null ")
		private String name;
		@ZipCode
		private String zipCode;
		//some other code
	}	

FooTest.java

	public class FooTest {
		
		private Validator validator; 
		
		@Before
		public void setUp() throws Exception {
			validator = Validation.buildDefaultValidatorFactory().getValidator();
		}
	
		@Test
		public void testFoo() {
			Foo foo = new Foo();
			foo.setName("foo1");
			foo.setZipCode("ABC");
			Set<ConstraintViolation<Foo>> cvs = validator.validate(foo);
			assertEquals("constraint violation size is 1?", 1, cvs.size());
			for (ConstraintViolation<?> cv : cvs) {
				assertEquals("violation message is?", ZipCode.MESSAGE, cv.getMessage());
			}
		}
	
		@After
		public void tearDown() throws Exception {
		}
	}

### @Valid级联检验

Bar内定义了Foo,使用`@Valid`注解当检验Bar时将会级联检验Foo是否符合要求

Bar.java

	public class Bar {
		@Valid
		private Foo foo;
	}

BarTest.java
	
	@Test
	public void testBar() {
		Bar bar = new Bar();
		bar.setFoo(new Foo());
		Set<ConstraintViolation<Bar>> cvs = validator.validate(bar);
		assertEquals("foo name violation?", 1, cvs.size());
	}
	
### 多值约束 Multiple Constraints

### 组合约束

### 组

### 组序列
	