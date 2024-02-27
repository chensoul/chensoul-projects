package org.springframework.security.oauth2.provider.token.store.jwk;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;

/**
 * Jwt set url Response entity
 * <p>
 * {
 * "keys" : [ {
 * "kty" : "RSA",
 * "e" : "AQAB",
 * "use" : "sig",
 * "kid" : "RmdoM2",
 * "alg" : "RS256",
 * "n" : "v5ZNiQ_TnBRQNWVM2yy70SxLX97dAavFK3aw875YnSJPWWpG24T8U-3bcWtyMX7DvVaIkNbCudukXxPGe9hCBJKTMifqxViqaDjkGswRY3mNocS4CMkhRLBdRbH7q6eayeqWbu8xpkxIa8eHdrghLMznKWlxsdCof6Wwhue8MNZw4vfZ3HF-PvuHC3yijNVHCJAQuRLuXrJvXngM90u5VPbLwe6oYxLXmgatW-3kPcB8XuGbRT33LAMef9P50e2_13zCIqLuhwhroZlmGkRo6v4LWlJzLZHIuaNXRtHmGLgPbr8eRJU16PM1hQZWFDkBY0PrzRZcZyfrceelIU3L3Q"
 * } ]
 * }
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
public class JwkSetResponses {

    private List<JwkSetResponse> keys;

    @Data
    public static class JwkSetResponse {
        private String kty;
        private String e;
        private String use;
        private String kid;
        private String alg;
        private String n;

        public JwkDefinition toJwkDefinition() {
            JwkDefinition.PublicKeyUse publicKeyUse =
                    JwkDefinition.PublicKeyUse.fromValue(use);
            JwkDefinition.CryptoAlgorithm algorithm =
                    JwkDefinition.CryptoAlgorithm.fromHeaderParamValue(alg);

            return new RsaJwkDefinition(kid, e, publicKeyUse, algorithm, n, e);
        }
    }

    public Set<JwkDefinition> toJwkDefinitions() {
        return this.keys.stream().map(JwkSetResponse::toJwkDefinition).collect(Collectors.toSet());
    }
}
