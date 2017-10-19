/*!
 * 通用脚本
 * sundongguo@20150412
 */
//≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡[通用模块]
//==================================================[Tappable]

var SXY = {
    icon100: 'https://upload.simuyun.com/logo/sxy100.png',
    icon200: 'https://upload.simuyun.com/logo/sxy200.png'
};

function getData() {
    var data = window.simuyun.getData();
    if (data) {
        data = data.split(':');
    }

    var token = data[0];
    var uid = data[1];
    var version = data[2];
    var role = data[3];
    var client = data[4];
    var mid = data[5];
    var mode = data[6];    // "1" 游客；  "2" 用户

    cookie.setItem('uid', uid);
    cookie.setItem('token', token);
    cookie.setItem('client', client);
    cookie.setItem('role', role);
    cookie.setItem('version', version);
    cookie.setItem('mid', mid);
    cookie.setItem('mode', mode);

}

(function() {
    if (window.simuyun) {
        getData();
    }
})();


//==================================================[Tappable]
/**
 * 为类名包含 tappable 的元素启用 tappable 模式。
 */
(function(window, document) {
    var $active = null;
    var activate = function($target) {
        if ($active) {
            $active.removeClass('active');
        }
        $active = $target;
        if ($active) {
            $active.addClass('active');
        }
    };

    if (navigator.support.touch) {
        var touchId = NaN;
        var startX = NaN;
        var startY = NaN;
        var getTouch = function(e) {
            var touch = null;
            Array.from(e.originalEvent.changedTouches).some(function(changedTouche) {
                if (changedTouche.identifier === touchId) {
                    touch = changedTouche;
                    return true;
                } else {
                    return false;
                }
            });
            return touch;
        };
        document
            .on('touchstart:relay(.tappable)', function(e) {
                if (!this.disabled) {
                    var touch = e.originalEvent.changedTouches[0];
                    touchId = touch.identifier;
                    startX = touch.clientX;
                    startY = touch.clientY;
                    activate(this);
                }
            })
            .on('touchmove', function(e) {
                var touch = getTouch(e);
                if (touch) {
                    var x = touch.clientX;
                    var y = touch.clientY;
                    if (Math.abs(x - startX) > 10 || Math.abs(y - startY) > 10) {
                        touchId = NaN;
                        activate(null);
                    }
                }
            })
            .on('touchend', function(e) {
                var touch = getTouch(e);
                if (touch) {
                    var x = touch.clientX;
                    var y = touch.clientY;
                    if (Math.abs(x - startX) > 10 || Math.abs(y - startY) > 10) {
                        touchId = NaN;
                        activate(null);
                    }
                }
                if (touch && $active && $active.contains(document.elementFromPoint(touch.clientX, touch.clientY))) {
                    $active.fire('tap', {bubbles: true, action: $active.getData('action')});
                }
            })
            .on('touchend, touchcancel', function(e) {
                var touch = getTouch(e);
                if (touch) {
                    touchId = NaN;
                    activate(null);
                }
            });
    } else {
        document
            .on('mousedown:relay(.tappable)', function(e) {
                if (e.which === 1 && !this.disabled) {
                    activate(this);
                }
            })
            .on('mouseup:relay(.tappable)', function(e) {
                if (e.which === 1 && $active === this) {
                    this.fire('tap', {bubbles: true, action: this.getData('action')});
                }
            })
            .on('mouseup', function(e) {
                if (e.which === 1 && $active) {
                    activate(null);
                }
            });
        window.on('blur', function() {
            if ($active) {
                activate(null);
            }
        });
    }

})(window, document);

//==================================================[信息提示]
/**
 * @新增属性
 * @新增方法
 *   show
 *     显示提示。
 *     参数：
 *       {string} message 要显示的文本信息。
 *     返回值：
 *       {Element} 本元素。
 * @新增事件
 */
var $info = function() {
    var $info = $('<div id="info" class="tappable"><span></span></div>');
    var $message = $info.find('span');
    document.on('beforedomready:once', function() {
        $info.insertTo(document.body);
    });
    var timer = undefined;
    $info.on('tap', function() {
        if (timer) {
            clearTimeout(timer);
        }
        $info.setStyle('display', 'none');
        $message.innerText = '';
    });
    return Object.mixin($info, {
        show: function(message) {
            $message.innerText = message;
            if (timer) {
                clearTimeout(timer);
            }
            timer = setTimeout(function() {
                $info.fade('out', {
                    duration: 200,
                    onFinish: function() {
                        $message.innerText = '';
                    }
                });
                timer = undefined;
            }, 1500);
            return $info.fade('in', {
                duration: 400
            });
        }
    });
}();

//==================================================[发送命令]
function sendCommand() {
    document.location = 'app:' + Array.from(arguments)
            .map(function(value) {
                return encodeURIComponent(value);
            })
            .join(':');
}

//==================================================[滚动到页首]
window.scrollTo(0, 0);

//==================================================[其他设置]
Request.options.minTime = 400;

var _Request = Request;
Request = function(url, options) {
    options = options || {};
    var token = cookie.getItem('token');
    var uid = cookie.getItem('uid');
    var client = cookie.getItem('client');
    var mid = cookie.getItem('mid');
    var version = cookie.getItem('version');

    var defaultOptions = {};
    defaultOptions.headers = {
        'Accept': '*/*',
        'token': token,
        'adviserId': uid,
        'mid': mid,
        'version': version
    };

    Object.mixin(options, defaultOptions);

    if (!!client) {
        options.headers['client'] = client;
    }

    options.headers['client'] = 'C';

    // options.headers['adviserId'] = "4aa0e114b8fb471d9008d57df747fcad";
    // options.headers['token'] = "2UXJbGtgdmVUpk3CdqREVzUB5+uWq2hHhgt6gfNoZRb7SgPHDXm4VIy4ElY+hJWOj0hb9XgPcHCe/8HgsfAJJFVZVOPNc/whCrgiTPZ6FV0=";

    options.contentType = options.contentType || 'application/json';
    options.maxTime = 15000;
    var _request = new _Request(url, options);
    _request.on('timeout', function() {
        if (token) {
            sendCommand('toastError', '当前网络信号较弱，页面打开较慢，请您耐心等待或返回重试');
        }
    });

    return _request;
};

var TokenRequest = function(url, options) {
    options = options || {};
    var token = cookie.getItem('token');
    var uid = cookie.getItem('uid');
    var mid = cookie.getItem('mid');
    var version = cookie.getItem('version');

    options.headers = {
        'Accept': '*/*',
        'token': token,
        'adviserId': uid,
        'mid': mid,
        'version': version
    };

    if (token && uid) {
        options.headers['token'] = token;
        options.headers['adviserId'] = uid;
    }

    options.headers['client'] = 'C';

    // options.headers['adviserId'] = "4aa0e114b8fb471d9008d57df747fcad";
    // options.headers['token'] = "2UXJbGtgdmVUpk3CdqREVzUB5+uWq2hHhgt6gfNoZRb7SgPHDXm4VIy4ElY+hJWOj0hb9XgPcHCe/8HgsfAJJFVZVOPNc/whCrgiTPZ6FV0=";

    options.contentType = options.contentType || 'application/json';

    return new _Request(url, options);
};

function processMoneyUnit(money) {
    if (money != null && money != '') {
        if (money >= 10000) {
            if (money % 10000 == 0) {
                return [(money / 10000).toFixed(2), '亿'];
            } else {
                return [(money / 10000).toFixed(2), '亿'];
            }
        } else {
            return [money, '万'];
        }
    } else {
        return [0, '万'];
    }
}
//--------------------------------邀请埋点信息
//---------------埋点信息
// var userIdM, userInfoStringM, userInfoM, categorytypeM;
// if (!!cookie.getItem('uid')) {
//     userIdM = cookie.getItem('uid');
//     userInfoStringM = localStorage.getItem(userIdM);
//     userInfoM = JSON.parse(userInfoStringM);
//     categorytypeM = userInfoM.toB.category || '';//彩云非彩云
//     var organizationM = userInfoM.toB.organizationName || "";//机构名称
//     if (categorytypeM == 5) {
//         categorytypeM = '1';
//     } else {
//         categorytypeM = '0';
//     }
// }

var SharingSettings = function() {
    return {
        Wechat: function(shareData) {
            var ua = navigator.userAgent.toLowerCase();
            if (ua.match(/MicroMessenger/i) == "micromessenger") { //在微信浏览器内
                var url = location.href.split('#')[0];

                function setWechatAPI(shareData) {
                    new Request('/auth/getSign', {noCache: true})
                        .on('complete', function(e) {
                            var data = null;
                            try {
                                data = JSON.parse(e.text);
                                wx.config({
                                    debug: false,
                                    appId: data.appId,
                                    timestamp: data.timestamp,
                                    nonceStr: data.nonceStr,
                                    signature: data.signature,
                                    jsApiList: [
                                        'onMenuShareTimeline',
                                        'onMenuShareAppMessage',
                                        'onMenuShareQQ',
                                        'onMenuShareWeibo'
                                    ]
                                });
                                wx.ready(function() {
                                    wx.onMenuShareAppMessage(shareData);
                                    wx.onMenuShareTimeline(shareData);
                                    wx.onMenuShareQQ(shareData);
                                    wx.onMenuShareWeibo(shareData);
                                });
                            } catch (e) {
                            }
                        })
                        .send({url: url});
                }

                var settings = {
                    link: url,
                    imgUrl: SXY.icon100
                };

                Object.mixin(settings, shareData);

                document.loadScript('//res.wx.qq.com/open/js/jweixin-1.0.0.js', {
                    onLoad: function() {
                        setWechatAPI(settings);
                    }
                });
            } else {

            }
        }
    };
}();

var UBC = function() {
    var ua = navigator.userAgent.toLowerCase();
    var inWechat = ua.match(/MicroMessenger/i) == "micromessenger";

    var APP = {
        appVersion: navigator.appVersion.toLocaleLowerCase(),
        isAndroid: function () {
            return (this.appVersion.indexOf("android") > 0);
        },
        isIOS: function () {
            return (this.appVersion.indexOf("iphone") > 0 || this.appVersion.indexOf("ipad") > 0);
        }
    };

    return {
        train: function() {
            var args = Array.from(arguments);
            if (args.length > 10) {
                args.length = 10;
            }

            var grp = args[0];
            var act = args[1];

            var options = [];
            for (var i = 2; i < args.length; i++) {
                options.push(args[i]);
            }

            if (inWechat) {
                var system = '';
                if (APP.isAndroid()) {
                    system = 'A';
                } else if (APP.isIOS()) {
                    system = 'I';
                } else {
                    system = 'P';
                }

                var config = {
                    "ip": '111.222.333.444', //server接收时转义
                    "mos": system,
                    "grp": grp,
                    "v": "h5",
                    "act": act
                };

                options.forEach(function(arg, index) {
                    config['arg' + (index + 1)] = arg;
                });

                var request = new _Request('https://muninubc.simuyun.com/simuyun-munin/training', {method: "POST"});
                request.on('complete', function(e) {

                });

                request.send({
                    contents: JSON.stringify([config])
                });
            } else {
                var c = ['mobClick', grp, act].concat(options);
                var delay = parseInt(Math.random() * 10);
                
                setTimeout(function() {
                    sendCommand.apply(null, c);
                }, delay);
            }
        }
    }
}();

var Share = function() {


    return {

    }
}();

var uid = cookie.getItem('uid');
// if (localStorage.getItem(uid)) {
new Request("/auth/v2/user/userInfo", {method: "GET"})

    .on("complete", function(e) {
        if (e.status == 200) {

            var data = {};
            try {
                data = JSON.parse(e.text).result;
            } catch (ex) {
            }


            var userLocalKey = uid;
            var sUserLocal = JSON.stringify(data);
            localStorage.setItem(userLocalKey, sUserLocal);

        }
    }).send({param: JSON.stringify({"adviserId": uid})});
// }

//≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡[脚本异常处理]
window.onerror = function(msg, url, lineNo, columnNo, error) {
    var string = msg.toLowerCase();
    var substring = "script error";
    if (string.indexOf(substring) > -1) {
        // alert('Script Error: See Browser Console for Detail');
    } else {
        if (!!lineNo && !!columnNo && lineNo > 1) {
            var path = window.location.pathname;
            var fileName = path.split("/").pop();

            //        var message = [
            //            'Message: ' + msg,
            //            'URL: ' + url,
            //            'Line: ' + lineNo,
            //            'Column: ' + columnNo,
            //            'Error object: ' + JSON.stringify(error)
            //        ].join(' - ');

            var uid = cookie.getItem('uid');
            var userInfo = JSON.parse(localStorage.getItem(uid));

            var content = '<div style="font: 16px/1.5 Consolas, Courier, SimSun, monospace;">' +
                '<p style="color: black;"><strong>Script Error</strong></p>' +
                '<p style="color: crimson;">' + msg + '</p>' +
                '<p style="color: dimgray; font-size: 14px;"><strong>FileName:</strong> ' + fileName + '(' + lineNo + ':' + columnNo + ')</p>' +
                '<p style="color: dimgray; font-size: 14px;"><strong>Location:</strong> ' + location.href + '</p>' +
                '<p style="color: dimgray; font-size: 14px;"><strong>UserAgent:</strong> ' + navigator.userAgent + '</p>' +
                '<p style="color: dimgray; font-size: 14px;"><strong>Screen:</strong> ' + screen.availWidth + ' x ' + screen.availHeight + '</p>' +
                '<p style="color: dimgray; font-size: 14px;"><strong>UserId:</strong> ' + uid + '</p>' +
                '<p style="color: dimgray; font-size: 14px;"><strong>UserName:</strong> ' + userInfo.realName + '</p>' +
                '<p style="color: dimgray; font-size: 14px;"><strong>Time:</strong> ' + new Date().toDateString() + '</p>' +
                '</div>';

            new Request('/api/web/log', {noCache: true, method: "POST", contentType: 'application/json'}).send({
                "appName": "app.intime.simuyun",
                "content": content
            });
        }
    }

    return false;
};
