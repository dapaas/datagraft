'use strict';

(function(scope){
    var channel = 'datagraft-post-message';

    function Grafterizer(origin, htmlElement) {
        this._stuffToDo = [];

        this.origin = origin;
        this._createIframe(htmlElement);
        this._sendMessageWindow = window;
        this._savedAuthorization = null;

        var _this = this;
        this.connected = false;
        window.addEventListener('message', function(event) {
            if (event.origin !== origin) return;

            var data = event.data;
            if (!data || !data.channel || data.channel !== channel) return;

            if (data.message === 'ready') {
                console.log("ready")
               _this.connected = true;
               _this._sendMessageWindow = event.source;
               _this._onReady();
            } else if (data.message === 'set-location') {
                window.location = data.location;
            }
        }, false);
    };

    Grafterizer.prototype.hideFooter = function() {
        for (var f = document.getElementsByTagName('footer'), i = 0, l = f.length; i < l;++i) {
            f[i].style.display = 'none';
        }
    };

    Grafterizer.prototype._createIframe = function(htmlElement) {
        var container = document.createElement('div');
        container.className = 'grafterizer-container';
        var iframe = document.createElement('iframe');
        iframe.className = 'grafterizer';
        iframe.src = this.origin + '/#/embedded';
        container.appendChild(iframe);
        htmlElement.appendChild(container);
    };

    Grafterizer.prototype._onReady = function() {
        for (var a = this._stuffToDo, i = 0, l = a.length; i < l; ++i) {
            this.sendMessage(a[i]);
        }
        this._stuffToDo = [];
        this.setAuthorization(this._savedAuthorization);
    };

    Grafterizer.prototype.sendMessage = function(message) {
        if (!this.connected) {
            this._stuffToDo.push(message);
            return this;
        }

        if (message !== Object(message)) {
            message = {content: message};
        }

        message.channel = channel;
        this._sendMessageWindow.postMessage(message, this.origin);
        return this;
    };

    Grafterizer.prototype.go = function(state, toParams) {
        return this.sendMessage({
            message: 'state.go',
            state: state,
            toParams: toParams
        });
    };

    Grafterizer.prototype.setAuthorization = function(keypass) {
        this._savedAuthorization = keypass;
        if (this.connected) {
            return this.sendMessage({
                message: 'set-authorization',
                keypass: keypass
            });
        }
        return this;
    };

    scope.Grafterizer = Grafterizer;

})(window);
