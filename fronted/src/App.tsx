import { useState } from "react"

function LikeButton() {
  const [liked, setLiked] =  useState(false)
  if(liked) return <div>you liked this</div>
  return (
    <button onClick={() => setLiked(true)}>LIKE</button>
  )
}

export default LikeButton
