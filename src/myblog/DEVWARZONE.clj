;;dev

(def post {:id "Bruce Wayne"
 :title "My first post"
 :body "How cool is this!?!?!?!?!"})



(defn post-summary [post]
  (let [id (:id post)
        title (:title post)
        body (:body post)]
    id
    title
    ))

(post-summary post)
