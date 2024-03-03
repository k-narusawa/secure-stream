package com.knarusawa.common.adapter.gateway.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GitHubUser(
    @JsonProperty("login")
    val login: String,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("node_id")
    val nodeId: String? = null,
    @JsonProperty("avatar_url")
    val avatarUrl: String? = null,
    @JsonProperty("gravatar_id")
    val gravatarId: String? = null,
    @JsonProperty("url")
    val url: String? = null,
    @JsonProperty("html_url")
    val htmlUrl: String? = null,
    @JsonProperty("followers_url")
    val followersUrl: String? = null,
    @JsonProperty("following_url")
    val followingUrl: String? = null,
    @JsonProperty("gists_url")
    val gistsUrl: String? = null,
    @JsonProperty("starred_url")
    val starredUrl: String? = null,
    @JsonProperty("subscriptions_url")
    val subscriptionsUrl: String? = null,
    @JsonProperty("organizations_url")
    val organizationsUrl: String? = null,
    @JsonProperty("repos_url")
    val reposUrl: String? = null,
    @JsonProperty("events_url")
    val eventsUrl: String? = null,
    @JsonProperty("received_events_url")
    val receivedEventsUrl: String? = null,
    val type: String? = null,
    @JsonProperty("site_admin")
    val siteAdmin: Boolean? = null,
    val name: String? = null,
    val company: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val email: String? = null,
    val hireable: Boolean? = null,
    val bio: String? = null,
    @JsonProperty("twitter_username")
    val twitterUsername: String? = null,
    @JsonProperty("public_repos")
    val publicRepos: Int? = null,
    @JsonProperty("public_gists")
    val publicGists: Int? = null,
    val followers: Int? = null,
    val following: Int? = null,
    @JsonProperty("created_at")
    val createdAt: String? = null,
    @JsonProperty("updated_at")
    val updatedAt: String? = null
)
