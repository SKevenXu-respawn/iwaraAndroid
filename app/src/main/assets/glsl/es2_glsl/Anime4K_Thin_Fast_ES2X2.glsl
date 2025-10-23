// Anime4K-v3.2-Thin-Fast  2× 超分  ES2 单文件版
precision highp float;

uniform sampler2D uTexture;     // 原始 640×360
uniform vec2      uResolution;  // 原始分辨率 (640,360)

/* 输出 1280×720，gl_FragCoord 范围 [0,1280]×[0,720] */
void main(){
    vec2  outRes = uResolution * 2.0;          // 1280,720
    vec2  srcPos = gl_FragCoord.xy / outRes;   // 0~1
    vec2  px     = 1.0 / uResolution;          // 原始像素步长

    /* ---- 简单边缘保持 + 双线性放大 ---- */
    vec2  t  = srcPos * uResolution + 0.5;     // 原始像素坐标
    vec2  f  = fract(t);                       // 小数部分
    vec2  base = (floor(t) - 0.5) * px;

    /* 4-tap 边缘检测 */
    float l = texture2D(uTexture, base + vec2(-px.x, 0.0)).r;
    float c = texture2D(uTexture, base                    ).r;
    float r = texture2D(uTexture, base + vec2( px.x, 0.0)).r;
    float u = texture2D(uTexture, base + vec2( 0.0,-px.y)).r;
    float d = texture2D(uTexture, base + vec2( 0.0, px.y)).r;

    float edge = abs(l - r) + abs(u - d);
    edge = pow(clamp(edge * 4.0, 0.0, 1.0), 0.7);

    /* 锐化核 */
    vec4 s = 5.0 * texture2D(uTexture, base)
           - texture2D(uTexture, base + vec2(-px.x, 0.0))
           - texture2D(uTexture, base + vec2( px.x, 0.0))
           - texture2D(uTexture, base + vec2( 0.0,-px.y))
           - texture2D(uTexture, base + vec2( 0.0, px.y));
    s = clamp(s, 0.0, 1.0);

    /* 双线性采样 + 边缘混合 */
    vec4 bilinear = texture2D(uTexture, srcPos);
    vec4 finalCol = mix(bilinear, s, edge * 0.8);

    gl_FragColor = finalCol;
}