[1mdiff --git a/src/main/java/com/codecool/poster_xd_api/servlets/UserServlet.java b/src/main/java/com/codecool/poster_xd_api/servlets/UserServlet.java[m
[1mindex 0e283e4..4942935 100644[m
[1m--- a/src/main/java/com/codecool/poster_xd_api/servlets/UserServlet.java[m
[1m+++ b/src/main/java/com/codecool/poster_xd_api/servlets/UserServlet.java[m
[36m@@ -58,42 +58,35 @@[m [mpublic class UserServlet extends PosterAbstractServlet<User, Post> {[m
 [m
     @Override[m
     protected void getObjectsForRoot(HttpServletRequest req, HttpServletResponse resp) throws IOException {[m
[31m-        List<User> userList = new ArrayList<>();[m
[31m-        List<List<User>> lists = new ArrayList<>();[m
[31m-        List<User> list1;[m
[31m-        List<User> list2;[m
[31m-        List<User> list3;[m
[31m-[m
[32m+[m[32m        Map<User, Integer> userIntegerMap = new HashMap<>();[m
[32m+[m[32m        List<User> userList;[m
[32m+[m[32m        int filterCounter = 0;[m
         String surname = req.getParameter("surname");[m
         if (surname != null) {[m
[31m-            list1 = dao.getObjectsByField("surname", surname);[m
[31m-            lists.add(list1);[m
[32m+[m[32m            List<User> list1 = dao.getObjectsByField("surname", surname);[m
[32m+[m[32m            for (User user : list1) {[m
[32m+[m[32m                userIntegerMap.merge(user, 1, Integer::sum);[m
[32m+[m[32m            }[m
[32m+[m[32m            filterCounter++;[m
         }[m
[31m-[m
         String name = req.getParameter("name");[m
         if (name != null) {[m
[31m-            list2 = dao.getObjectsByField("name", name);[m
[31m-            lists.add(list2);[m
[32m+[m[32m            List<User> list2 = dao.getObjectsByField("name", name);[m
[32m+[m[32m            for (User user : list2) {[m
[32m+[m[32m                userIntegerMap.merge(user, 1, Integer::sum);[m
[32m+[m[32m            }[m
[32m+[m[32m            filterCounter++;[m
         }[m
[31m-[m
         String isActive = req.getParameter("isActive");[m
         if (isActive != null) {[m
[31m-            list3 = dao.getObjectsByField("isActive", isActive);[m
[31m-            lists.add(list3);[m
[32m+[m[32m            List<User> list3 = dao.getObjectsByField("isActive", isActive);[m
[32m+[m[32m            for (User user : list3) {[m
[32m+[m[32m                userIntegerMap.merge(user, 1, Integer::sum);[m
[32m+[m[32m            }[m
[32m+[m[32m            filterCounter++;[m
         }[m
[31m-[m
[31m-        if (!lists.isEmpty()) {[m
[31m-            userList = lists.get(0);[m
[31m-        }[m
[31m-[m
[31m-//        Set<User> result = Sets.newHashSet(lists.get(0));[m
[31m-//        for (List<User> numbers : list) {[m
[31m-//            result = Sets.intersection(result, Sets.newHashSet(numbers));[m
[31m-//        }[m
[31m-//        for (List<User> list : lists) {[m
[31m-//[m
[31m-//        }[m
[31m-[m
[32m+[m[32m        int finalFilterCounter = filterCounter;[m
[32m+[m[32m        userList = userIntegerMap.entrySet().stream().filter(e->e.getValue() == finalFilterCounter).map(Map.Entry::getKey).collect(Collectors.toList());[m
         String objectsAsJsonString = userList.stream().map(e->(Jsonable)e).map(Jsonable::toJson).collect(Collectors.joining(",\n"));[m
         writeObjectsToResponseFromCollection(resp, objectsAsJsonString);[m
 [m
