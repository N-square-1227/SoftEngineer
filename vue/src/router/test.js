var id2=4
var a={
  id:2,
  id2,
  showId:function (){
    console.log(this.id);
  },
  showName(name){
    console.log(name)
  },
  b:(res)=>{
    console.log(res);
    console.log(this.id);
  },
  users:[
    "数据结构",
    "数据库"
  ]

};
console.log(a.users[0]);
console.log(a.id2);
a.showName("计算机网络"),
a.showId();
a.b("xyz");
