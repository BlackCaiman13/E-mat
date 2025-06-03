package ci.scia.e_mat.etat;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the libelleEtat value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = EtatLibelleEtatUnique.EtatLibelleEtatUniqueValidator.class
)
public @interface EtatLibelleEtatUnique {

    String message() default "{Exists.etat.libelleEtat}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class EtatLibelleEtatUniqueValidator implements ConstraintValidator<EtatLibelleEtatUnique, String> {

        private final EtatService etatService;
        private final HttpServletRequest request;

        public EtatLibelleEtatUniqueValidator(final EtatService etatService,
                final HttpServletRequest request) {
            this.etatService = etatService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(etatService.get(Long.parseLong(currentId)).getLibelleEtat())) {
                // value hasn't changed
                return true;
            }
            return !etatService.libelleEtatExists(value);
        }

    }

}
