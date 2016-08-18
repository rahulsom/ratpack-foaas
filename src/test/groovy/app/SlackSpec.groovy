package app

class SlackSpec extends FunctionalSpec {

  def "slack integration"() {
    given:
    requestSpec {
      it.body.type('application/x-www-form-urlencoded')
      it.body.text("""
token=gIkuvaNzQIHg97ATvDxqgjtO&
team_id=T0001&
team_domain=example&
channel_id=C2147483705&
channel_name=test&
user_id=U2147483697&
user_name=Steve&
command=/foaas&
text=steviewonder bob&
response_url=https://hooks.slack.com/commands/1234/5678
""")
    }

    expect:
    postText("slack") == '{"response_type":"in_channel","text":"I just called, to say..........bob FUCK OFF!\\n- Steve"}'
  }

}

