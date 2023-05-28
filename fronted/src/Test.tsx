import { useEffect } from "react";

function Test() {

  const getData = async () => {
    const res = await fetch(
      "http://localhost:8080/api/get"
    ).then((res) =>  console.log(res), cons);
    
  };

  useEffect(() => {
    getData(); //API 호출
  }, []);

  return;
}
export default Test
