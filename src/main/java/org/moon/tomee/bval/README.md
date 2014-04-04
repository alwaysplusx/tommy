### Bean Validation [Bean Validation 技术规范特性概述](http://www.ibm.com/developerworks/cn/java/j-lo-beanvalid/)

javax.validation.constraints包下提供一些基础的约束注解,一些Bean Validation都实现了这些基础约束注解的检验

javax.validation.constraints包下包括`@NotNull` `@Null` `@Size` `@Min` `@Max` `@Digits`等等

通过`javax.validation.Validation`可以加载类路径下的具体Bean Validation实现

### 用户自定义约束注解

必须包含三个主要方法`String message()` `Class<?> group()` `Class<? extends Payload> payload()`

`@Constraint`用于指定检验的执行者,检验者均实现`javax.validation.ConstraintValidator`

<b>ZipCode.java</b>

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

<b>Foo.java</b>

Foo中zipCode使用`@ZipCode`表明该值需要检验

	public class Foo {
		@NotNull(message = "foo.name can't be null ")
		private String name;
		@ZipCode
		private String zipCode;
		//some other code
	}	

<b>FooTest.java</b>

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

<b>Bar.java</b>

	public class Bar {
		@Valid
		private Foo foo;
	}

<b>BarTest.java</b>
	
	@Test
	public void testBar() {
		Bar bar = new Bar();
		bar.setFoo(new Foo());
		Set<ConstraintViolation<Bar>> cvs = validator.validate(bar);
		assertEquals("foo name violation?", 1, cvs.size());
	}
	
### 多值约束 Multiple Constraints

<b>Contains.java</b>

	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Constraint(validatedBy = { ContainsValidator.class })
	public @interface Contains {
	
		String content() default "";
	
		String message() default "may not be contain declare content";
	
		Class<?>[] groups() default {};
	
		Class<? extends Payload>[] payload() default {};
		
		@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
		@Retention(RUNTIME)
		@Documented
		@interface List {
			Contains[] value();
		}
	}

<b>Baz.java</b>

	public class Baz {

		@Contains.List(
			value = {
				@Contains(content = "this is automata"),
				@Contains(content = "this is manual machines") 
			}
		)
		private String text;
	}

> 多值约束条件为AND,如果中@Contains.List有一条@Contains不符合要求还是有检验异常消息

### 组合约束

Bean Validation 规范允许将不同的约束进行组合来创建级别较高且功能较多的约束，从而避免原子级别约束的重复使用

检验时将会对用户自定义的约束上的`@NotNull` `@Size`先进行验证

	@NotNull(message = "idcard can't be null")
	@Size(min = 15, max = 18, message = "idcard length may not be right")
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Constraint(validatedBy = { IDCardValidator.class })
	public @interface IDCard {
	
		String message() default "idcard is may not be right";
	
		Class<?>[] groups() default {};
	
		Class<? extends Payload>[] payload() default {};
	
	}

<b>Citizen.java</b>

	public class Citizen {
	
		@IDCard
		private String idCard;
	}	

<b>CitizenTest.java</b>

	@Test
	public void testCitizen() {
		Citizen citizen = new Citizen();
		//citizen.setIdCard("111111111111111");
		Set<ConstraintViolation<Citizen>> cvs = validator.validate(citizen);
		for(ConstraintViolation<?> cv : cvs){
			System.out.println(cv.getMessage());
		}
		assertEquals("violation is not empty", false, cvs.isEmpty());
	}

<b>Test Result</b>

	idcard can't be null

### 组

Bean Validation 规范中组定义了约束的子集。对于一个给定的 Object Graph 结构，有了组的概念，则无需对该 Object Graph 中所有的约束进行验证，只需要对该组定义的一个子集进行验证即可。

<b>Animal.java</b>

	public interface Animal {
		@NotNull(message = "animal name may not be null")
		String getName();
		@NotNull(message = "animal ownerName may not be null")
		String getOwnerName();
	}

<b>Dog.java</b>
	
	public class Dog implements Animal {
	
		private String name;
		private String ownerName;
		@NotNull(message = "dog type may not be empty")
		private String type;
		@NotNull(message = "dog age may not be empty", groups = { Animal.class })
		private Integer age;
	}
	
<b>DogTest.java</b>	

	@Test
	public void testDog() {
		Dog dog = new Dog();
		Set<ConstraintViolation<Dog>> cvs = validator.validate(dog, Animal.class);
		for(ConstraintViolation<?> cv : cvs){
			System.out.println(cv.getMessage());
		}
		assertEquals("violation size is 3", 3, cvs.size());
	}

<b>Test Result</b>

	animal ownerName may not be null
	dog age may not be empty
	animal name may not be null
	
> 组别验证需要在约束声明时进行组别的声明，否则使用默认的组 Default.class.

### 组序列

默认情况下，不同组别的约束验证是无序的，然而在某些情况下，约束验证的顺序却很重要

如下面两个例子：
（1）第二个组中的约束验证依赖于一个稳定状态来运行，而这个稳定状态是由第一个组来进行验证的。
（2）某个组的验证比较耗时，CPU 和内存的使用率相对比较大，最优的选择是将其放在最后进行验证。

因此，在进行组验证的时候尚需提供一种有序的验证方式，这就提出了组序列的概念。

一个组可以定义为其他组的序列，使用它进行验证的时候必须符合该序列规定的顺序。
在使用组序列验证的时候，如果序列前边的组验证失败，则后面的组将不再给予验证。	
	
<b>Group interface</b>

	interface FirstNameGroup {
	}
	
	interface MiddleNameGroup {
	}
	
	interface LastNameGroup {
	}
	
<b>MyGroup.java</b>
	
	@GroupSequence(value = { FirstNameGroup.class, MiddleNameGroup.class, LastNameGroup.class })
	public interface MyGroup {
	
	}

<b>Username.java</b>	

	public class Username {
		@NotNull(message = "first name may not be null", groups = { FirstNameGroup.class })
		private String firstName;
		@NotNull(message = "middle name may not be null", groups = { MiddleNameGroup.class })
		private String middleName;
		@NotNull(message = "last name may not be null", groups = { LastNameGroup.class })
		private String lastName;
	}
	
<b>Username.Test</b>	

	@Test
	public void testUsername() {
		Username username = new Username();
		Set<ConstraintViolation<Username>> cvs = validator.validate(username, MyGroup.class);
		for (ConstraintViolation<?> cv : cvs) {
			System.out.println(cv.getMessage());
		}
		assertEquals("violation size is 1", 1, cvs.size());
	}
	
<b>Test Result</b>	

	first name may not be null
	