"use strict";(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[366],{8697:function(se,b,r){r.r(b),r.d(b,{default:function(){return W}});var N=r(15009),u=r.n(N),T=r(97857),F=r.n(T),E=r(99289),h=r.n(E),R=r(5574),C=r.n(R),n=r(31324);function $(m,i){return y.apply(this,arguments)}function y(){return y=h()(u()().mark(function m(i,l){return u()().wrap(function(M){for(;;)switch(M.prev=M.next){case 0:return M.abrupt("return",(0,n.request)("/api/login/captcha",F()({method:"GET",params:F()({},i)},l||{})));case 1:case"end":return M.stop()}},m)})),y.apply(this,arguments)}var j=r(1413),f=r(67294),k={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M832 464h-68V240c0-70.7-57.3-128-128-128H388c-70.7 0-128 57.3-128 128v224h-68c-17.7 0-32 14.3-32 32v384c0 17.7 14.3 32 32 32h640c17.7 0 32-14.3 32-32V496c0-17.7-14.3-32-32-32zM332 240c0-30.9 25.1-56 56-56h248c30.9 0 56 25.1 56 56v224H332V240zm460 600H232V536h560v304zM484 701v53c0 4.4 3.6 8 8 8h40c4.4 0 8-3.6 8-8v-53a48.01 48.01 0 10-56 0z"}}]},name:"lock",theme:"outlined"},H=k,O=r(84089),I=function(i,l){return f.createElement(O.Z,(0,j.Z)((0,j.Z)({},i),{},{ref:l,icon:H}))};I.displayName="LockOutlined";var z=f.forwardRef(I),G={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M744 62H280c-35.3 0-64 28.7-64 64v768c0 35.3 28.7 64 64 64h464c35.3 0 64-28.7 64-64V126c0-35.3-28.7-64-64-64zm-8 824H288V134h448v752zM472 784a40 40 0 1080 0 40 40 0 10-80 0z"}}]},name:"mobile",theme:"outlined"},Q=G,A=function(i,l){return f.createElement(O.Z,(0,j.Z)((0,j.Z)({},i),{},{ref:l,icon:Q}))};A.displayName="MobileOutlined";var D=f.forwardRef(A),K=r(87547),p=r(3552),X=r(14670),V=r(12461),Y=r(42640),v={container:"container___REQAy",lang:"lang___DAjLY",content:"content___IkUx1",icon:"icon___fqAhh"},e=r(85893),w=function(i){var l=i.content;return(0,e.jsx)(X.Z,{style:{marginBottom:24},message:l,type:"error",showIcon:!0})},J=function(){var i=(0,f.useState)({}),l=C()(i,2),L=l[0],M=l[1],q=(0,f.useState)("account"),P=C()(q,2),S=P[0],_=P[1],B=(0,n.useModel)("@@initialState"),x=B.initialState,te=B.refresh,ee=B.setInitialState,o=(0,n.useIntl)(),ne=function(){var d=h()(u()().mark(function s(){var t,c;return u()().wrap(function(a){for(;;)switch(a.prev=a.next){case 0:return a.next=2,x==null||(t=x.fetchUserInfo)===null||t===void 0?void 0:t.call(x);case 2:if(c=a.sent,!c){a.next=6;break}return a.next=6,ee(function(re){return F()(F()({},re),{},{currentUser:c})});case 6:case"end":return a.stop()}},s)}));return function(){return d.apply(this,arguments)}}(),ae=function(){var d=h()(u()().mark(function s(){var t,c;return u()().wrap(function(a){for(;;)switch(a.prev=a.next){case 0:return a.prev=0,t=new URL(window.location.href).searchParams,n.history.push(t.get("redirect")||"/"),a.abrupt("return");case 6:a.prev=6,a.t0=a.catch(0),c=o.formatMessage({id:"pages.login.failure",defaultMessage:"\u767B\u5F55\u5931\u8D25\uFF0C\u8BF7\u91CD\u8BD5\uFF01"}),console.log(a.t0),V.ZP.error(c);case 11:case"end":return a.stop()}},s,null,[[0,6]])}));return function(){return d.apply(this,arguments)}}(),U=L.status,Z=L.type;return(0,e.jsx)("div",{children:(0,e.jsx)("div",{className:v.content,children:(0,e.jsxs)(p.U0H,{title:"Spark",initialValues:{autoLogin:!0},onFinish:function(){var d=h()(u()().mark(function s(t){return u()().wrap(function(g){for(;;)switch(g.prev=g.next){case 0:return g.next=2,ae(t);case 2:case"end":return g.stop()}},s)}));return function(s){return d.apply(this,arguments)}}(),children:[(0,e.jsx)(Y.Z,{activeKey:S,onChange:_,centered:!0,items:[{key:"account",label:o.formatMessage({id:"pages.login.accountLogin.tab",defaultMessage:"\u8D26\u6237\u5BC6\u7801\u767B\u5F55"})},{key:"mobile",label:o.formatMessage({id:"pages.login.phoneLogin.tab",defaultMessage:"\u624B\u673A\u53F7\u767B\u5F55"})}]}),U==="error"&&Z==="account"&&(0,e.jsx)(w,{content:o.formatMessage({id:"pages.login.accountLogin.errorMessage",defaultMessage:"\u8D26\u6237\u6216\u5BC6\u7801\u9519\u8BEF(admin/ant.design)"})}),S==="account"&&(0,e.jsxs)(e.Fragment,{children:[(0,e.jsx)(p.VaQ,{name:"username",fieldProps:{size:"large",prefix:(0,e.jsx)(K.Z,{className:v.prefixIcon})},placeholder:o.formatMessage({id:"pages.login.username.placeholder",defaultMessage:"\u7528\u6237\u540D: admin or user"}),rules:[{required:!0,message:(0,e.jsx)(n.FormattedMessage,{id:"pages.login.username.required",defaultMessage:"\u8BF7\u8F93\u5165\u7528\u6237\u540D!"})}]}),(0,e.jsx)(p.VaQ.Password,{name:"password",fieldProps:{size:"large",prefix:(0,e.jsx)(z,{className:v.prefixIcon})},placeholder:o.formatMessage({id:"pages.login.password.placeholder",defaultMessage:"\u5BC6\u7801: ant.design"}),rules:[{required:!0,message:(0,e.jsx)(n.FormattedMessage,{id:"pages.login.password.required",defaultMessage:"\u8BF7\u8F93\u5165\u5BC6\u7801\uFF01"})}]})]}),U==="error"&&Z==="mobile"&&(0,e.jsx)(w,{content:"\u9A8C\u8BC1\u7801\u9519\u8BEF"}),S==="mobile"&&(0,e.jsxs)(e.Fragment,{children:[(0,e.jsx)(p.VaQ,{fieldProps:{size:"large",prefix:(0,e.jsx)(D,{className:v.prefixIcon})},name:"mobile",placeholder:o.formatMessage({id:"pages.login.phoneNumber.placeholder",defaultMessage:"\u624B\u673A\u53F7"}),rules:[{required:!0,message:(0,e.jsx)(n.FormattedMessage,{id:"pages.login.phoneNumber.required",defaultMessage:"\u8BF7\u8F93\u5165\u624B\u673A\u53F7\uFF01"})},{pattern:/^1\d{10}$/,message:(0,e.jsx)(n.FormattedMessage,{id:"pages.login.phoneNumber.invalid",defaultMessage:"\u624B\u673A\u53F7\u683C\u5F0F\u9519\u8BEF\uFF01"})}]}),(0,e.jsx)(p.BXt,{fieldProps:{size:"large",prefix:(0,e.jsx)(z,{className:v.prefixIcon})},captchaProps:{size:"large"},placeholder:o.formatMessage({id:"pages.login.captcha.placeholder",defaultMessage:"\u8BF7\u8F93\u5165\u9A8C\u8BC1\u7801"}),captchaTextRender:function(s,t){return s?"".concat(t," ").concat(o.formatMessage({id:"pages.getCaptchaSecondText",defaultMessage:"\u83B7\u53D6\u9A8C\u8BC1\u7801"})):o.formatMessage({id:"pages.login.phoneLogin.getVerificationCode",defaultMessage:"\u83B7\u53D6\u9A8C\u8BC1\u7801"})},name:"captcha",rules:[{required:!0,message:(0,e.jsx)(n.FormattedMessage,{id:"pages.login.captcha.required",defaultMessage:"\u8BF7\u8F93\u5165\u9A8C\u8BC1\u7801\uFF01"})}],onGetCaptcha:function(){var d=h()(u()().mark(function s(t){var c;return u()().wrap(function(a){for(;;)switch(a.prev=a.next){case 0:return a.next=2,$({phone:t});case 2:if(c=a.sent,c!==!1){a.next=5;break}return a.abrupt("return");case 5:V.ZP.success("\u83B7\u53D6\u9A8C\u8BC1\u7801\u6210\u529F\uFF01\u9A8C\u8BC1\u7801\u4E3A\uFF1A1234");case 6:case"end":return a.stop()}},s)}));return function(s){return d.apply(this,arguments)}}()})]}),(0,e.jsxs)("div",{style:{marginBottom:24},children:[(0,e.jsx)(p.V2E,{noStyle:!0,name:"autoLogin",children:(0,e.jsx)(n.FormattedMessage,{id:"pages.login.rememberMe",defaultMessage:"\u81EA\u52A8\u767B\u5F55"})}),(0,e.jsx)("a",{style:{float:"right"},children:(0,e.jsx)(n.FormattedMessage,{id:"pages.login.forgotPassword",defaultMessage:"\u5FD8\u8BB0\u5BC6\u7801"})})]})]})})})},W=J}}]);
