import model from '@/plugins/model';
//防抖
export const debounce = (func, delay) => {
    let timeId = null
    return function (...args) {
        clearTimeout(timeId);
        timeId = setTimeout(() => {
            func.apply(this, args);
        }, delay);
    };
};
//节流  
export const throttle = (func, limit, error) => {
    let inThrottle = true;  // 初始时就设置节流状态为true，直接开始节流
    setTimeout(() => inThrottle = false, limit);  // 在限定时间后解除节流
    return function (...args) {
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);  // 如果不处于节流状态，则执行函数
            inThrottle = true;  // 再次激活节流状态
            setTimeout(() => inThrottle = false, limit);  // 设置定时器再次解除节流状态
        } else {
            if (error) {
                // 如果提供了错误消息，使用msgError方法显示
                model.msgError(error);
            }
        }
    };
};

