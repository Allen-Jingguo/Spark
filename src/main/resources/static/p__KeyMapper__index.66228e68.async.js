"use strict";(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[971],{89961:function(ce,E,a){a.r(E),a.d(E,{default:function(){return oe}});var G=a(5574),p=a.n(G),K=a(97857),o=a.n(K),R=a(3552),C=a(12461),z=a(26713),y=a(71577),B=a(65360),c=a(81579),U=a(71230),F=a(15746),L=a(69677),k=a(34041),D=a(68116),J=a(59652),m=a(67294),W=a(15009),u=a.n(W),Y=a(99289),g=a.n(Y),x=a(31324),H=function(){var l=g()(u()().mark(function t(r,n){return u()().wrap(function(s){for(;;)switch(s.prev=s.next){case 0:return s.abrupt("return",(0,x.request)("/api/mapper/list",o()({method:"POST",data:o()({},r)},n||{})));case 1:case"end":return s.stop()}},t)}));return function(r,n){return l.apply(this,arguments)}}();function Q(l,t){return w.apply(this,arguments)}function w(){return w=g()(u()().mark(function l(t,r){return u()().wrap(function(i){for(;;)switch(i.prev=i.next){case 0:return i.abrupt("return",(0,x.request)("/api/mapper/create_new",o()({method:"POST",data:o()({},t)},r||{})));case 1:case"end":return i.stop()}},l)})),w.apply(this,arguments)}var V=function(){var l=g()(u()().mark(function t(){return u()().wrap(function(n){for(;;)switch(n.prev=n.next){case 0:return n.abrupt("return",(0,x.request)("/api/mapper/get_all_key_mapper_type",{method:"GET"}));case 1:case"end":return n.stop()}},t)}));return function(){return l.apply(this,arguments)}}(),X=function(){var l=g()(u()().mark(function t(r,n){return u()().wrap(function(s){for(;;)switch(s.prev=s.next){case 0:return s.abrupt("return",(0,x.request)("/api/mapper/view",o()({method:"GET",params:o()({},r)},n||{})));case 1:case"end":return s.stop()}},t)}));return function(r,n){return l.apply(this,arguments)}}(),q=function(){var l=g()(u()().mark(function t(r,n){return u()().wrap(function(s){for(;;)switch(s.prev=s.next){case 0:return s.abrupt("return",(0,x.request)("/api/mapper/disable",o()({method:"POST",params:o()({},r)},n||{})));case 1:case"end":return s.stop()}},t)}));return function(r,n){return l.apply(this,arguments)}}(),f={getList:H,view:X,disable:q,createNew:Q,getAllMapperType:V},_=a(85402),ee=a(81961),e=a(85893),te=function(t){var r=(0,m.useState)(!1),n=p()(r,2),i=n[0],s=n[1],A=(0,m.useState)([]),h=p()(A,2),N=h[0],T=h[1],b=c.Z.useForm(),I=p()(b,1),Z=I[0],M=function(){Z.submit()},$=function(){t.closeCreateModal()};(0,m.useEffect)(function(){f.getAllMapperType().then(function(v){T(v.data)})},[]);var S=function(P){s(!0),f.createNew(o()({},P)).then(function(d){d.success&&(C.ZP.success("operate successfully!"),t.closeCreateModal(!0)),s(!1)})};return(0,e.jsx)(e.Fragment,{children:(0,e.jsx)(_.Z,{title:"Create New Mapper",visible:!0,onOk:M,confirmLoading:i,onCancel:$,children:(0,e.jsx)("div",{style:{display:"flex",justifyContent:"center",alignItems:"center"},children:(0,e.jsxs)(c.Z,{form:Z,name:"advanced_search",className:"ant-advanced-search-form",layout:"vertical",onFinish:S,labelAlign:"right",children:[(0,e.jsx)(c.Z.Item,{name:"name",label:"Name (unique)",rules:[{required:!0,message:"Missing  Name"}],children:(0,e.jsx)(L.Z,{style:{width:400}})}),(0,e.jsx)(c.Z.Item,{name:"keyMapperType",label:"Type",rules:[{required:!0,message:"Missing Outbound Type"}],children:(0,e.jsx)(k.Z,{options:N,style:{width:400}})}),(0,e.jsx)(c.Z.Item,{name:"properties",label:"Mapper Template ( Complex  templates will be supported in the future )",children:(0,e.jsx)(L.Z.TextArea,{style:{width:400,height:200},showCount:!0,placeholder:"input  template text "})}),(0,e.jsx)(c.Z.Item,{label:"Upload",valuePropName:"fileList",children:(0,e.jsx)(ee.Z,{action:"/api/formatter/upload",listType:"picture-card",children:(0,e.jsx)("div",{children:(0,e.jsx)("div",{style:{marginTop:8},children:"Upload"})})})})]})})})})},ae=te,ue=function(t){keyMapperApi.view(_objectSpread({},t)).then(function(r){console.log(r),r&&message.success("start success!")})},re=function(t){console.log(t),f.disable(o()({},t)).then(function(r){console.log(r),r&&C.ZP.success("operate successfully!")})},ne=function(t){console.log(t),C.ZP.success("Canceled")},se=[{title:"Name",dataIndex:"name",key:"name",render:function(t){return(0,e.jsx)("a",{children:t})}},{title:"Type",dataIndex:"type"},{title:"Created Time",dataIndex:"createdTime"},{title:"Action",key:"action",render:function(t,r){return(0,e.jsxs)(z.Z,{size:"middle",children:[(0,e.jsx)(y.Z,{type:"primary",onClick:function(){C.ZP.success("Wait for the implementation !")},children:"Detail"}),(0,e.jsx)(B.Z,{title:"Are you sure to start this flow?",onConfirm:function(){return re(r)},onCancel:ne,okText:"Yes",cancelText:"No",children:(0,e.jsx)(y.Z,{type:"primary",children:"disable"})})]})}}],le=function(){var t=c.Z.useForm(),r=p()(t,1),n=r[0],i=(0,m.useState)([]),s=p()(i,2),A=s[0],h=s[1],N=(0,m.useState)([]),T=p()(N,2),b=T[0],I=T[1],Z=(0,m.useState)(!1),M=p()(Z,2),$=M[0],S=M[1];(0,m.useEffect)(function(){f.getAllMapperType().then(function(d){I(d.data)}),f.getList({}).then(function(d){d.success&&h(d.data)})},[]);var v=function(O){f.getList(O).then(function(j){j.success&&h(j.data)})},P=function(O){O&&f.getList({}).then(function(j){j.success&&h(j.data)}),S(!1)};return(0,e.jsxs)(R._zJ,{children:[(0,e.jsxs)(J.Z,{style:{borderRadius:8},bodyStyle:{backgroundImage:"radial-gradient(circle at 97% 10%, #EBF2FF 0%, #F5F8FF 28%, #EBF1FF 124%)"},children:[(0,e.jsx)("div",{children:(0,e.jsx)(c.Z,{form:n,name:"advanced_search",className:"ant-advanced-search-form",onFinish:v,children:(0,e.jsxs)(U.Z,{gutter:24,children:[(0,e.jsx)(F.Z,{children:(0,e.jsx)(c.Z.Item,{name:"name",label:"Namey",children:(0,e.jsx)(L.Z,{})})}),(0,e.jsx)(F.Z,{children:(0,e.jsx)(c.Z.Item,{name:"type",label:"Type",children:(0,e.jsx)(k.Z,{options:b,style:{width:150}})})}),(0,e.jsxs)(F.Z,{style:{textAlign:"left"},children:[(0,e.jsx)(y.Z,{type:"primary",htmlType:"submit",children:"Search"}),(0,e.jsx)(y.Z,{style:{marginLeft:10},type:"primary",onClick:function(){n.resetFields()},children:"Clear"}),(0,e.jsx)(y.Z,{style:{marginLeft:10},type:"primary",onClick:function(){S(!0)},children:"Create New"})]})]})})}),(0,e.jsx)("div",{style:{marginTop:20},children:(0,e.jsx)(D.Z,{columns:se,dataSource:A,pagination:!0})})]}),$&&(0,e.jsx)(ae,{closeCreateModal:P})]})},ie=le,oe=ie}}]);