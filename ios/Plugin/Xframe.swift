import Foundation

@objc public class Xframe: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
