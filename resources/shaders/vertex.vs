#version 400 core

in vec3 position;

out vec3 colour;

uniform mat4 transformationMatrix;

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position,1.0);
    gl_Position = worldPosition;
    colour = vec3(0.859,0.973,1.);
}