import appioscombined
import Strings
import SwiftUI
import Theme

struct SessionSpeakersView: View {

    let speakers: [TimetableSpeaker]

    var body: some View {
        VStack(alignment: .leading) {
            Text(L10n.Session.speaker)
                .font(Font.system(size: 16, weight: .medium, design: .default))
                .padding(.bottom)

            ForEach(self.speakers, id: \.self) { speaker in
                HStack {
                    AsyncImage(
                        url: URL(string: speaker.iconUrl),
                        content: {
                            $0.image?.resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(width: 60, height: 60)
                                .clipShape(Circle())
                        }
                    )
                    .padding(.trailing)
                    Text(speaker.name)
                        .font(Font.system(size: 16, weight: .regular, design: .default))
                        .foregroundColor(AssetColors.onPrimaryContainer.swiftUIColor)
                }
            }
        }
    }
}

#if DEBUG
struct SessionSpeakersView_Previews: PreviewProvider {
    static var previews: some View {
        SessionSpeakersView(speakers: TimetableItem.Session.companion.fake().speakers)
    }
}
#endif
