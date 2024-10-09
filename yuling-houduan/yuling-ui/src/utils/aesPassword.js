import CryptoJS from 'crypto-js';
//获取IV16
function getIV16() {
    return CryptoJS.lib.WordArray.random(16).toString(CryptoJS.enc.Hex);
}
//生成自定义密钥
function getKey(IV16,jobNumber) {
    const combined = jobNumber + IV16 + "veauwd177" + "030920";
    const hashed = CryptoJS.SHA256(combined);
    //返回的实际是一个字节数组
    return CryptoJS.enc.Hex.parse(hashed.toString().substring(0, 64));
}
//AES加密方法
function aesEncryption(iv, key, password) {
    const ivBytes = CryptoJS.enc.Hex.parse(iv);
    const encrypted = CryptoJS.AES.encrypt(password, key, {
        iv: ivBytes,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7,
    });
    return encrypted.toString();
}

export { getIV16, getKey, aesEncryption };