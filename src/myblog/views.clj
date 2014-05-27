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


;; Setting up the admin blog page
;; -------------------------------

(defn get-posts-map [username conn]
       ;postings is all the posts based on a certain user
    (let [postings (d/q '[
         :find ?user ?tit ?bod
         :in $ ?u
         :where
             [?c :posts/username ?user]
             [?c :posts/title ?tit]
             [?c :posts/body ?bod]
             [?c :posts/username ?u]] (d/db conn) username)]
        ;Then returns a ziped map of the postings for post-summary to use
   (map #(zipmap [:username :title :body] %) postings)))

(defn post-summary [post]
  (let [username (:username post)
        title (:title post)
        body  (:body post)]
     [:section
      [:h3 title]
      [:section body]
      [:section.actions
        [:a {:href (str "/admin/" username "/edit")} "Edit"] " / "
        [:a {:href (str "/admin/" username "/delete")} "Delete"]]]
    );let
  )


; put all posts on a page
(defn admin-blog-page [username conn]
  (layout "My Blog - Administer Blog"
    [:h1 "Administer Blog"]
    [:h2 "All my posts"]
    [:a {:href "/admin/add"} "Add"]
 ;; get all the posts for a certain user
  (map #(post-summary %) (get-posts-map username conn))
          ))

; (def fake-posts  [{:username "Bruce Wayne"
;                   :title "My first post"
;                    :body "How cool is this!?!?!?!?!"}
;
;                   {:username "Bruce Wayne"
;                    :title "My Second post"
;                    :body "Things are starting to heat up!!!"}])
