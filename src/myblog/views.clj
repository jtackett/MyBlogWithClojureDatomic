(ns myblog.views
  (:require [hiccup.core :refer (html)]
            [hiccup.form :as f]
            [myblog.posts :as posts]
            [datomic.api :as d]))

(defn layout [title & content]
  (html
    [:head [:title title]]
    [:body content]))

(defn main-page []
  (layout "My Blog"
    [:h1 "My Blog"]
    [:p "Welcome to my page about clojure and datomic"]))

; Post is a map corresponding to a record from the database
;break down individual posts
(defn post-summary [post]
  (let [id    (:id post)
        title (:title post)
        body  (:body post)]
     [:section
      [:h3 title]
      [:section body]
      [:section.actions
        [:a {:href (str "/admin/" id "/edit")} "Edit"] " / "
        [:a {:href (str "/admin/" id "/delete")} "Delete"]]]
    )
  )



;; till I figure out how to store posts as maps
;; sample post map
 (def fake-posts  [{:id "Bruce Wayne"
                   :title "My first post"
                   :body "How cool is this!?!?!?!?!"}
                   {:id "Bruce Wayne"
                   :title "My Second post"
                   :body "Things are starting to heat up!!!"}])

; put all posts on a page
(defn admin-blog-page []
  (layout "My Blog - Administer Blog"
    [:h1 "Administer Blog"]
    [:h2 "All my posts"]
    [:a {:href "/admin/add"} "Add"]
 ;; get all the posts for a certain user
 ;; post-summary grabs each post and turns
 ;; it into html for the page then this function
          ;; returns all the posts for the page
    (map #(post-summary %) fake-posts) ))


 ;; query for individual posts
; (defn get-all-user-posts [username]
;    [let user-id    ((d/q '[:find ?posts
;                             :where [?post :posts/post ?posts]]
;                      (d/db conn))
;                       ffirst)])


