(function() {
    // 配置
    var envir = 'online';
    var configMap = {
        online: {
           appkey: 'e3d44f9d4c0fa9509f0e82e85db9b5d2',
           url:'http://localhost:8008/awcp'
        }
    };
    window.CONFIG = configMap[envir];
}())