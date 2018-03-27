unless ARGV[1].empty?
	require "chatwork"

  # Create message
  ChatWork.api_key = "INSERT_YOUR_CHATWORK_KEY"
  ChatWork::Message.create(room_id: "#{ARGV[0]}", body: "[info]Please Check :x:p \nLink : #{ARGV[1]}[hr]#{ARGV[2]}[/info]")
end
