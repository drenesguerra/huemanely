package coom.huemanely.application.email

// class AwsSesEmailSender(private val config: TenantEmailConfig) : EmailSender {
//     override fun send(email: Email) {
//         val sesClient = SesClient.builder()
//             .region(Region.of(config.host ?: "ap-southeast-2"))
//             .credentialsProvider {
//                 AwsBasicCredentials.create(config.username, config.password)
//             }.build()

//         sesClient.sendEmail {
//             it.destination { d -> d.toAddresses(email.to) }
//             it.source(config.fromAddress)
//             it.message {
//                 it.subject { s -> s.data(email.subject) }
//                 it.body { b -> b.text { t -> t.data(email.body) } }
//             }
//         }
//     }
// }