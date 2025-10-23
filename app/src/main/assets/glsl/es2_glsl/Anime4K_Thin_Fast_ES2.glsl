// Anime4K-v3.2-Thin-(Fast) 手动合并版 · ES2 可直接编译
precision highp float;

uniform sampler2D uTexture;
uniform vec2      uResolution;      // 输入纹理分辨率（像素）
varying vec2      vTexCoord;

/* ---------- 工具 ---------- */
vec4 hook(vec2 p) { return texture2D(uTexture, p); }
float get_luma(vec4 c){ return dot(c.rgb, vec3(0.299, 0.587, 0.114)); }

/* ---------- Pass1：Luma ---------- */
vec4 pass1(vec2 pos){
    return vec4(get_luma(hook(pos)), 0.0, 0.0, 0.0);
}

/* ---------- Pass2：Sobel X ---------- */
vec4 pass2(vec2 pos){
    vec2 px = 1.0 / uResolution;
    float l = pass1(pos + vec2(-px.x, 0.0)).x;
    float c = pass1(pos).x;
    float r = pass1(pos + vec2( px.x, 0.0)).x;
    float xgrad = (-l + r);
    float ygrad = (l + c + c + r);
    return vec4(xgrad, ygrad, 0.0, 0.0);
}

/* ---------- Pass3：Sobel Y + 梯度 ---------- */
vec4 pass3(vec2 pos){
    vec2 px = 1.0 / uResolution;
    float tx = pass2(pos + vec2(0.0, -px.y)).x;
    float cx = pass2(pos).x;
    float bx = pass2(pos + vec2(0.0,  px.y)).x;
    float ty = pass2(pos + vec2(0.0, -px.y)).y;
    float by = pass2(pos + vec2(0.0,  px.y)).y;
    float xgrad = (tx + cx + cx + bx) / 8.0;
    float ygrad = (-ty + by)        / 8.0;
    float norm  = sqrt(xgrad*xgrad + ygrad*ygrad);
    return vec4(pow(norm, 0.7));
}

/* ---------- Pass4：Gaussian X（简化 5-tap） ---------- */
vec4 pass4(vec2 pos){
    vec2 px = 1.0 / uResolution;
    float sigma = uResolution.y / 1080.0;
    float g = 0.0, gn = 0.0;
    for (int i = -2; i <= 2; ++i){
        float di = float(i);
        float gf = exp(-0.5 * (di*di) / (sigma*sigma));
        g  += pass3(pos + vec2(di * px.x, 0.0)).x * gf;
        gn += gf;
    }
    return vec4(g / gn, 0.0, 0.0, 0.0);
}

/* ---------- Pass5：Gaussian Y ---------- */
vec4 pass5(vec2 pos){
    vec2 px = 1.0 / uResolution;
    float sigma = uResolution.y / 1080.0;
    float g = 0.0, gn = 0.0;
    for (int i = -2; i <= 2; ++i){
        float di = float(i);
        float gf = exp(-0.5 * (di*di) / (sigma*sigma));
        g  += pass4(pos + vec2(0.0, di * px.y)).x * gf;
        gn += gf;
    }
    return vec4(g / gn, 0.0, 0.0, 0.0);
}

/* ---------- Pass6：Warp 迭代 1 次 ---------- */
vec4 pass6(vec2 pos){
    const float STRENGTH = 0.6;
    vec2 px = 1.0 / uResolution;
    vec2 uv = pos;
    vec2 dn = pass5(uv).xy;
    vec2 dd = dn / (length(dn) + 0.01) * px * (uResolution.y / 1080.0) * STRENGTH;
    uv -= dd;
    return hook(uv);
}

/* ---------- 入口 ---------- */
void main(){
    gl_FragColor = pass6(vTexCoord);
}