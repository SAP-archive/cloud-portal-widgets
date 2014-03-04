/**
 * Created with JetBrains RubyMine.
 * User: i061485
 * Date: 6/9/13
 * Time: 3:37 PM
 * Login form widget.
 */

var login, Login = function Login(opts) {

    /**
     * Define $content
     * @type {*|jQuery|HTMLElement}
     */
    this.$content = $('#container');

    /**
     * Define current user instance
     * @type {undefined}
     */
    this.currentUser = undefined;

    /**
     * Define opts
     * @type {*}
     */
    this.opts = opts;

    this.init();
};

$.extend(true, Login.prototype, {
    /**
     * Init OS Gadget
     * @returns {boolean}
     */
    initGadget: function initGadget() {
        if (typeof gadgets === 'undefined') {
            return false;
        }
        this.gadget = new gadgets.GadgetPrefs();
    },
    /**
     * Init widget
     */
    init: function init() {
        this.initGadget();

        this.root = window.parent;

        this._checkGuestAccountCallback();

        this.renderContent();
        this.setForm();

        this.$content.html('').append(this.$html);
        this._bindSubmit();
    },
    /**
     * Check guest user
     * @private
     */
    _checkGuestAccountCallback: function _checkGuestAccountCallback() {
        /**
         * Update current user info
         * @type {*}
         */
        this.currentUser = this.root.ODP.currentUser;
    },
    /**
     * Render relevant content
     */
    renderContent: function renderContent() {
        this.interval = window.setInterval(
           this._checkCurrentUser.bind(this),
            500
            );
    },
    /**
     * Check if current user is available
     * @returns {boolean}
     * @private
     */
    _checkCurrentUser: function _checkCurrentUser() {
        if (typeof this.currentUser === 'undefined') {
            return false;
        }

        if (this.currentUser.firstname === 'Guest') {
            return false;
        }

        window.clearInterval(this.interval);

        this.welcome();
        this.$content.html('').append(this.$html);

    },
    /**
     * Get current user name
     * @returns {string}
     * @private
     */
    _getUserName: function _getUserName() {
        return [
            this.currentUser.firstname,
            this.currentUser.lastname
        ].join(' ');
    },
    /**
     * Render welcome content
     */
    welcome: function welcome() {
        var tempText = "Click to join JAM discussions";
        this.$content.addClass('welcome').bind("click", function() {
            gadgets.sap.navigation.navigate(1);
        });;
        
        this.$html = [
            '<p class="afterText">',
            '<span id="sp">',
            tempText,
            '</span>',
            '</p>'
        ].join('');
    },
    /**
     * Set form html
     * @param {String} text
     */
    setForm: function setForm() {
        this.$html = [
          //  '<h2>Join our JAM Discussions</h2>',
            '<form id="loginform" method="post" action="/j_security_check" target="login_form_frame">',
            '<ul>',
            this.opts.text,
            this._renderLi('User Name', 'text', 'username', 1),
            this._renderLi('Password', 'password', 'password', 2),
            '</ul>','<img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEIAAAAhCAYAAABtNH0cAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNSBNYWNpbnRvc2giIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6M0JDMjhCQzczM0NFMTFFMEExNkRBQzM5MkUxNDRFQUUiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6M0JDMjhCQzgzM0NFMTFFMEExNkRBQzM5MkUxNDRFQUUiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDozQkMyOEJDNTMzQ0UxMUUwQTE2REFDMzkyRTE0NEVBRSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDozQkMyOEJDNjMzQ0UxMUUwQTE2REFDMzkyRTE0NEVBRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PvouZlgAAAXDSURBVHja7JhZTBtHGIB/O2tsoPjgDkfsRkACIcVA26RBUUzaNEBSxUpfWvXBtrSK+tBGoCov7QMgVVVPmVA1qSpZwEur9KE1aqWmqpQ4LzRqLpODSIEUQ4jDaTtAAYOxOzOLje3d9cERkMhI413Pzvzzzzf//PPPCOAXpw+ep3eE6Gd+i0MwwUnFBQq8PmrLIhAITPC2gsavFLjdi+gp3HIQhEITvJtF+/8iEPNb0xIMO+jgoq0HAkN4v4AOL0YgFkIK5GIh6PfIQVcsB3W6mCXHOu4Gy9AMXHn8H5j7pjj7anszB1RSEau8+eoYaRuc9CWorxJZRN1dbi90j8+R/qxjc6tYDgjC6WKakw982Y01SyRKlSrAeDgHwdgWk1wXci/NXSPQcn08UKaSiaD/VDFn/fa7TjD88SikrKkqCxoPZMU8FtvkPDRcsoO5dzJOS0C7w5kympdRbqJgETwe0BfLoK02P2YIjPVsA5sDzRBq7894hvmStlAaUpfkRW9c41FJE+BXrYroy5LFmxcjQiAg8pNFC6oUCoxv5MVtaZaBKTDfdzCDWcq6vWkRwWGrC64PvpXFc23HlaDJSw6VxZW9XhN8XEFHk0fJEwS+/TkykEvYlmBzuaHDOhb4X5adDBqVNFC34aINdbYY+I6/qWQJETs8USiH9pujywU8ICy2yRC5XEn3UhpY/nVFdoyN++hYwFLJKJw6pEzh/Fh+/ja45jyscr06A1RyCVjt06GKlaVH7VC7WwHyBOGyXB4Q1W33Au9qNAGXDSVoAkJjPw3W2+vlh9C0PyYIPqSDULhkslzJWKMkSpPOgnL7zRFoujQQUobr4UHGkvQYWKAtz9IIkm21T8HZridsfyEXs3RbkmmC5tfouDaUWbfHFz6zAYXLM6H/o0ow1r4I6qykiGuRzLSEHa2beybYJo3kRvUR4X3EWg9D+LQqLggCgQCET2c80HlvnN/BocHVH8iBWx+UQ/+ZV8B4bCfjB/BMBmUyuPCY48k0dNwYZpWrtycjc09i2vINMEg2jm10FZlR6yFZJvjsIL0S50sNOGdg0OmG9uvDoH85O/LWpZBAfVUuyc1/2aAJZX+5Zid72+y4NgyWPie3o6vIAutQHy+Iptd3kKcyVQLaknSQJ3JYG55Av4/AccIX1SuCQEAMOmaT8YvhQg92GwjG9pgaNh5RgVIhRu3ug76SOyAy3x0F18w8eo6BtjQj1GnuSYeGzge8ILD8aKkTyQUfBoEc41eHVwyB+Ihg0zL81APV526A5aEzNqeHoOkrs0HHAc/6eApsE7NELlGYw7rwTK80jsA6tv9jZ3zC16uDwJw1wrYfS6+DZJUiEbR7M0D3ag6oc1N4BRhPFHGabeedsYDZWh44uGMKZBUDjvjPDuY7o2D4sYeB0HJk1RCYlXX6TzRtIInoG1ITwXhyFwKTGZdwm2OWhOCaAv5tteXKANQfUkY/18x6yASdRfWX/I4JWo+uCQQGxIcXAyD0+3LBfHuEdMoZeLQeXfNTMYaFQbMCqtZry1ba52Bfr31bQ6+lHlROSoLI/tQNmsJUaHuvFM38bjh72UaAWIeYYzY2/ca6wnW5HuCCwCynCf47xu9q6bXWg8p4IWHa7pqTNdYVBA26APz/NyxxO1ETnKuj16M7KokSLGgKUolFrImpo53C3D0C6jxp2BpfQJBFoCmKsZ/w0BvHCeeP0evFnZKKKagJ2+NXk8zdw9Dwcw/nN3W+FG59cjB+i8AHqHWEQOKIvx+6qAYUTFV/cxXM1pHo3ntmAZp/72ViBI7U0TWE4zLObB2c5G3HBrGU8XL4fn0hMAZ36rfAVR3xEUkiYtaaXewLFuujSQSLOTtoitLidXIBq8BLJGrAROQgS/jhOP0sXBIGgSMa8Sa8b0YQ3qJ9MUae+AS5uhB7cyYC4Vl2KHwOYXOC2BAImw3EhkHwg9gMjvLzjYSA0/8CDAB6Up2krsGTjgAAAABJRU5ErkJggg=="/>',
            '<button title="Enter here">Log On</button>',
            '<iframe id="login_form_frame" name="login_form_frame"></iframe>',
            '</form>'
        ].join('');
    },
    /**
     * Render internal form fields
     * @param {String} text
     * @param {String} type
     * @param {String} rel
     * @param {Number} index
     * @returns {string}
     * @private
     */
    _renderLi: function _renderLi(text, type, rel, index) {
        return [
            '<li>',
            '<label for="j_', rel, '">', text, '</label><br />',
            '<input tabindex="', index, '" title="', text, '" type="', type, '" id="j_', rel, '" name="j_', rel, '"/>',
            '</li>'
        ].join('');
    },
    /**
     * Bind submit
     * @private
     */
    _bindSubmit: function _bindSubmit() {
        this._unbindSubmit().bind(
            'click.submit',
            /**
             * Submit
             */
                function submit(e) {
                    e.preventDefault();

                    this._renderLoader();

                    this._loginCallback.bind(this)();

                }.bind(this)
                );
        },
    /**
     * Render login loader
     * @private
     */
    _renderLoader: function _renderLoader() {
        $('<div />').
            addClass('loader').
            appendTo(this.$content).
            append(
            $('<img />').attr({
            src: "data:image/gif;base64,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",
            alt: 'Loader'
        })
            );
    },
    /**
     * RPC Login callback
     * @param {*} root          //Parent window
     * @param {jQuery} parent$  //Parent jQuery
     * @private
     */
    _loginCallback: function _loginCallback() {
        $('#loginform', this.$content).submit();
        setTimeout(this.afterLoginCallback.bind(this), 3000);
    },
    afterLoginCallback: function afterLoginCallback() {
        this.root.location.reload(true);
    },
    /**
     * Unbind submit
     * @private
     */
    _unbindSubmit: function _unbindSubmit() {
        return $('button', this.$content).unbind('click.submit');
    }
});


$('document').ready(
    /**
     * On ready fn
     */
        function onReady() {
            login = new Login({
                text: ''
            });
        }
    );


                   