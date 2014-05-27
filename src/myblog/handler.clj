(ns myblog.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [myblog.views :as views]
            [datomic.api :as d]
            [ring.util.response :as resp]
            [ring.middleware.basic-authentication :refer :all]))

(comment SETUP  ________________________________________)
(defn create-empty-db []
  (def uri "datomic:dev://localhost:4334/MSGBlog")
		(d/delete-database uri)
		(d/create-database uri)
    (def conn (d/connect uri))
    (def schema (load-file "src/myblog/dbschema.edn"))
			(d/transact conn schema))

(defn refresh-schema-db []
  (def uri "datomic:dev://localhost:4334/MSGBlog")
    (d/create-database uri)
    (def conn (d/connect uri))
    (def schema (load-file "src/myblog/dbschema.edn"))
      (d/transact conn schema))

(defn refresh-data[]
  (def data (load-file "src/myblog/dbUserData.edn"))
  (d/transact conn data))

(create-empty-db)
(refresh-data)

;; Server handling

;;Authentication
;; based on the database I might add

(defn find-usernames []
  (d/q '[:find ?usernames :where [?eid :user/username ?usernames]] (d/db conn)))

(defn find-password [username]
  (d/q '[:find ?p
         :in $ ?u
         :where
             [?c :user/password ?p]
             [?c :user/username ?u]] (d/db conn) username))

(defn authenticated? [username pass]
      ;I def user here so that I can find the right admin-blog-page
  (def user username)
          ;I used first to get inside the persistent vectors
    (and (some #(= username (first %)) (find-usernames))
         (= pass (ffirst(find-password username)))))

;;Defining routes
;; GET "/whatever" is the URL extension
(defroutes public-routes
  (GET "/" [] (views/main-page))
  (route/resources "/")
  )

(defroutes protected-routes
    ;admin-blog-page is in views and returns posts
    ; This needs to capture the username inputed into authenticated?
    ; NEEDS UPDATE
  (GET "/admin" [] (views/admin-blog-page user conn))
  )

(defroutes app-routes
  public-routes
  (wrap-basic-authentication protected-routes authenticated?)
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

