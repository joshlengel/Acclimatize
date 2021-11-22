#version 330

in vec3 position;
in vec3 normal;
in vec3 color;

out vec3 _normal;
out vec3 _color;
out vec3 toCam;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

void main() {
    vec4 worldPos = model * vec4(position, 1.0);
    gl_Position = projection * view * worldPos;
    _normal = (vec4(normal, 0.0) * model).xyz;
    _color = color;

    toCam = (inverse(view) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPos.xyz;
}