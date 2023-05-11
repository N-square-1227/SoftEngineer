const axios=require('axios')

axios({
  url:'http://localhost:8877/users/test',
  method:'GET',
}).then(res=>{
  console.log(res.data);
})
