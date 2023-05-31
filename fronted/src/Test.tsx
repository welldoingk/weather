import axios from "axios";
import { useState } from "react";



function Test() {
  const [data, setData] = useState(null);

  const getData2 = async () => {
    await axios
      .get("http://localhost:8080/api/get")
      .then((res) => setData(res.data));
    console.log(data);
    
  };

  function Bb() {
    return (
      <>
        <table>
          <thead>
            <tr>
              <td>no</td>
              <td>baseDate</td>
              <td>baseTime</td>
              <td>category</td>
              <td>fcstDate</td>
              <td>fcstTime</td>
              <td>fcstValue</td>
              <td>nx</td>
              <td>ny</td>
            </tr>
          </thead>
          <tbody>
            {data &&
              data.map((data) => (
                <tr key={data.no}>
                  <td>{data.no}</td>
                  <td>{data.baseDate}</td>
                  <td>{data.baseTime}</td>
                  <td>{data.category}</td>
                  <td>{data.fcstDate}</td>
                  <td>{data.fcstTime}</td>
                  <td>{data.fcstValue}</td>
                  <td>{data.nx}</td>
                  <td>{data.ny}</td>
                </tr>
              ))}
          </tbody>
        </table>
      </>
    );
  }
  // useEffect(() => {
  //   getData(); //API 호출
  // }, []);

  return (
    <>
      <button onClick={() => getData2()}>btn</button>
      <Bb />
    </>
  );
}
export default Test;
