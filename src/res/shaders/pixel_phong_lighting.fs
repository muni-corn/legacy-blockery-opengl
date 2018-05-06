#version 130

// The colour that we passed in through the vertex shader.
in vec4 varyingColor;
// The normal that we passed in through the vertex shader.
in vec3 varyingNormal;
// The vertex that we passed in through the vertex shader.
in vec4 varyingVertex;

void main() {
    vec3 vertexPosition = (gl_ModelViewMatrix * varyingVertex).xyz;
    
    vec3 surfaceNormal = normalize((gl_NormalMatrix * varyingNormal).xyz);
    
    vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vertexPosition);
    float diffuseLightIntensity = max(0, dot(surfaceNormal, lightDirection));
    
    lightDirection = normalize(gl_LightSource[1].position.xyz - vertexPosition);
    diffuseLightIntensity += max(0, dot(surfaceNormal, lightDirection));
    
    lightDirection = normalize(gl_LightSource[2].position.xyz - vertexPosition);
    diffuseLightIntensity += max(0, dot(surfaceNormal, lightDirection));
    
    if (diffuseLightIntensity > 1.0) {
    	diffuseLightIntensity = 1.0; 
    }
	gl_FragColor.rgb += (diffuseLightIntensity) * varyingColor.rgb;
    
    gl_FragColor += gl_LightModel.ambient;
    
    if (gl_FragColor.r > 1.0) {
   		gl_FragColor.r = 1.0;
    }
    
    if (gl_FragColor.g > 1.0) {
    	gl_FragColor.g = 1.0;
    }
    
    if (gl_FragColor.b > 1.0) {
    	gl_FragColor.b = 1.0;
    }
    
    gl_FragColor.a = varyingColor.a;

    //vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));
    
    //float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
    
    //if (diffuseLightIntensity != 0) {
    //  float fspecular = pow(specular, gl_FrontMaterial.shininess);
    //	gl_FragColor.rgb += fspecular;
    //}
}