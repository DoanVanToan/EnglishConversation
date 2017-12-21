unless ARGV[1].empty?
	require "chatwork"

  # Create message
  ChatWork.api_key = "9e7226c65900123de5226edc8b912be0"
  ChatWork::Message.create(room_id: "#{ARGV[0]}", body: "[info]Please Check :x:p \nLink : #{ARGV[1]}[hr]#{ARGV[2]}[/info]")
end
