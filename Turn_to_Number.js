const fs = require('fs');

let rawdata = fs.readFileSync('./output.json');
const arr = JSON.parse(rawdata);

arr.forEach(val => {
    for(let key in val) Number.isNaN(+val[key]) || (val[key] = +val[key])
});

console.log(arr);
let data = JSON.stringify(arr, null, 2);
fs.writeFileSync('output-mongo-with-number.json', data);