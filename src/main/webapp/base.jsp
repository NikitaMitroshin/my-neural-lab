<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="en">
<head>
    <title>Cat or Dog recognizer</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
    <style>
        .my-container {
            width: 500px;
            height: 100px;
            text-align: center;
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            margin: auto;
        }

        .dog-img {
            margin-top: 45%;
            margin-left: 20%;
        }

        .img-upload {
            margin-left: 30%;
        }
    </style>
</head>
<body>

<div class="container">

    <div class="my-container">
        <c:if test="${petType != null}">
            <h3> There is ${petType}" on image that you've just uploaded</h3>
            <br/>
            <br/>
        </c:if>

        <h1>Cat or Dog Recognizer</h1>
        <img src="http://www.nyan.cat/cats/original.gif" alt="cat">
        <br/>
        <form class="inner" method="post" action="/recognizeCatOrDog" enctype="multipart/form-data">
            <br/><br/> Please upload photo:
            <input class="img-upload" type="file" name="image" accept="image/*,image/jpeg" required/><br/><br/>
            <input type="submit" value="Upload"/>
        </form>
    </div>
    <img class="dog-img" src="http://www.nyan.cat/cats/fiesta.gif" alt="dog">

</div>

</body>
</html>
