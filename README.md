<p>This project aims to develop a RESTful API for a social media platform that allows users to register, login, create posts, exchange messages, follow other users, and retrieve their activity feed.</p>
<ol>
    <li><strong>Authentication and Authorization</strong>
        <ul>
            <li>Users can register by providing a username, email, and password.</li>
            <li>Users can login by providing valid credentials.</li>
        </ul>
    </li>
  <li><strong>Post Management</strong>
        <ul>
            <li>Users can create new posts by specifying text, title, and attaching images.</li>
            <li>Users can view posts created by other users.</li>
            <li>Users can update and delete their own posts.</li>
        </ul>
    </li>
    <li><strong>User Interactions</strong>
        <ul>
            <li>Users can send friend requests to other users. Once a request is sent, the requesting user remains a subscriber until they unsubscribe. If the recipient accepts the request, both users become friends. If the request is declined, the requesting user remains a subscriber.</li>
            <li>Friends are also subscribers of each other.</li>
            <li>If one friend removes the other, they also unsubscribe. The second user should remain a subscriber.</li>
            <li>Friends can exchange messages.</li>
        </ul>
    </li>
    <li><strong>Subscriptions and Activity Feed</strong>
        <ul>
            <li>The user's activity feed should display the latest posts from users they are subscribed to.</li>
        </ul>
    </li>
</ol>
<h2>Technologies and Tools</h2>
<ul>
    <li>Programming Language: Java 17</li>
    <li>Framework: Spring Boot 3 </li>
    <li>Database: PostgreSQL</li>
    <li>Authentication and Authorization: Spring Security</li>
    <li>API Documentation: Swagger (host:port/swagger-ui)</li>
</ul>
