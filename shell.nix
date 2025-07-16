let
  nixpkgs = fetchTarball "https://github.com/NixOS/nixpkgs/tarball/nixos-unstable";
  pkgs = import nixpkgs { config = {}; overlays = []; };
in

pkgs.mkShellNoCC {
  packages = with pkgs; [
    jdk21
  ];


  shellHook = ''
      alias compile='mkdir -p bin && javac -d bin $(find . -name "*.java")'
      alias build='compile && jar cvfm AnalLex.jar manifest.txt -C bin app6'
      alias run='java -jar AnalLex.jar'
      alias clean='rm -rf bin *.jar'

      echo "✅ Java dev shell ready! Use: compile, build, run, clean"
  '';
}
