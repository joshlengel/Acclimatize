#version 130

precision mediump float;

in vec3 _normal;
in vec3 _color;
in vec3 toCam;

out vec4 fragment;

uniform vec3 skyColor;
uniform vec3 sunDir = normalize(vec3(0.1, -0.8, 0.2));
uniform float shineDamp = 100.0;
uniform float reflectivity = 0.1;

uniform float fogMin = 20.0;
uniform float fogMax = 30.0;

void main() {
    // Lighting
    vec3 normal = normalize(_normal);
    float diffuseFactor = max(dot(normal, -sunDir), 0.1);
    vec3 diffuseColor = _color;

    float camDist = length(toCam);

    float specularFactor = reflectivity * pow(max(dot(reflect(sunDir, normal), toCam / camDist), 0.0), shineDamp);
    vec3 specularColor = vec3(1.0f);

    vec3 lightColor = diffuseColor * diffuseFactor + specularColor * specularFactor;

    // Fog
    float fogFactor = smoothstep(fogMin, fogMax, camDist);

    fragment = vec4(mix(lightColor, skyColor, fogFactor), 1.0);
}