package ec.edu.upse.gcf.util;

import java.util.Map;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class Validar extends AbstractValidator {
	public void validate(ValidationContext ctx) {
		//all the bean properties
		Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());

		//first let's check the passwords match		
		validaEmail(ctx, (String)beanProps.get("email").getValue());
		validateCaptcha(ctx, (String)ctx.getValidatorArg("captcha"), (String)ctx.getValidatorArg("captchaInput"));			
	}
	private void validaEmail(ValidationContext ctx, String email) {
		if(email == null || !email.matches(".+@.+\\.[a-z]+")) {
			this.addInvalidMessage(ctx, "email", "Email no existente o correo inválido.!");            
		}
	}

	private void validateCaptcha(ValidationContext ctx, String captcha, String captchaInput) {
		if(captchaInput == null || !captcha.equals(captchaInput)) {
			this.addInvalidMessage(ctx, "captcha", "EL captcha no es el correcto!");
		}
	}	
}
