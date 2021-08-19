package my.springcloud;

import static org.assertj.core.api.Assertions.*;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"local"})
@SpringBootTest
class StringEncryptorTest {

	@Autowired
	private StringEncryptor jasyptStringEncryptor;

	@BeforeEach
	void setUp() {

	}

	@Test
	void testEncryption() {
		System.out.println(System.getProperty("jasypt.encryptor.password"));

		String plainText = "jdbc:log4jdbc:mysql://146.56.160.235:3306/account";
		String encText = this.jasyptStringEncryptor.encrypt(plainText);
		System.out.println(encText);
		assertThat(plainText).isEqualTo(this.jasyptStringEncryptor.decrypt(encText));

		plainText = "account";
		encText = this.jasyptStringEncryptor.encrypt(plainText);
		System.out.println(encText);
		assertThat(plainText).isEqualTo(this.jasyptStringEncryptor.decrypt(encText));

		plainText = "!@#Account123";
		encText = this.jasyptStringEncryptor.encrypt(plainText);
		System.out.println(encText);
		assertThat(plainText).isEqualTo(this.jasyptStringEncryptor.decrypt(encText));
	}

}
