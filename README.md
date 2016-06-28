# game
This is NOT open source project.

[Previous version](https://drive.google.com/drive/folders/0Bw0pbRfg9sLaRUc5dFAyZ2ZxZHM)

## Description
This is a repository of game project.
First commit is 20160624 version. If you want to get a previous version, please access Google Drive.

All files are commited from Eclipse directly.

## Introduciton and how to merge


1.新規登録は各自で済ませましょう。

2.SweetsGPのGitページアクセスして、ページの右上になるforkボタンをクリック。
  すると、自分のGitページに飛びます。

3.Forkして自分のレポジトリにゲームのプロジェクトがあることを確認。

〜〜〜〜〜初回時のみ行ってください〜〜〜〜〜

4.端末(ターミナル)を開いて、元のプロジェクトのクローン先を作りましょう。
　(端末からなら好きな階層にmkdir (フォルダ名) を打ち込むか、普通にフォルダ作成かしましょう。)

5.端末で作成したフォルダに移動後、git clone (自分のGitページにフォークしたプロジェクトがあるディレクトリのパス)を入力して、編集用のワークスペースを作成します。その後、引っ張ってきたディレクトリに移動します。
  (例:git clone https://github.com/(Githubに登録したユーザ名)/game.git cd /game)

〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜


6.Eclipseでクローンしたプロジェクトを編集します。既存のワークスペースから作業場所を変える場合は各自設定をしておいてください。端末の参照ディレクトリには注意してください。

7.編集後、git branch でコンソールに作成した名前が出てくるかでわかります。この段階では master の前に * がついているはずです。

8.ブランチの確認後、git diff と打ち込むことで編集したコードの差分が表示されます。正しく編集されていいるかどうかここで始めの確認が行えます。(確認が終えたらCtrl + Zで終了)

9.次に、git status と打ち込むと、自分が編集したファイルのパスがコンソール上に出てきます。
  出てきた文字の色が緑色なら次の 10 へ進んでください。赤色なら、編集したデータを置く土台が無いということになるので、土台を作るために git add (赤色で表示されたパス) を打ち込んで土台を作ります。(複数ある場合は同じことを他のにも適用しましょう。)
  addが一通り行えれば、もう一度git status と打ち込んで緑色で表示されればOKです。

10.次に作成したレポジトリにコミットを行います。git commit -m (修正した内容に対する簡単なコメント) を打ち込んで files changed, inserctions などの文字が出てこればOKです。
  次に、更新が行えたかをgit logで確認します。すると、更新履歴がコンソールに表示されるので、一番上に自分の打ち込んだコメントが付いている更新履歴が表示されているかを確認します。

11.最後に、編集した内容を元のマスターブランチに適用させます。git push origin を打ち込むと、Githubの登録の時に入力したユーザー名とパスワードを要求されるので打ち込みましょう。(されない時もあり)
  最後に、master->masterがコンソールに出力されればOKです。

12.次に、Githubの自分のWebページにアクセスして、フォークしたプロジェクトにアクセスします。アクセス後、「New pull request」というボタンを押すと、自分の編集した内容を送る画面に移動します。
  このプルリク元と先が自分の作成したブランチから元のゲームプロジェクトのブランチになっているかを確認し、確認できたら適当にTITLEをつけてリクエストを送ります。

13.その後、SweetGPのGithubアカウントにログインして、自分の送ったリクエストが届いているはずです。
  リクエストページに移動して、Mergeのボタンを押して統合完了です。

お疲れ様でした、わからないことがあれば北山か大上に聞いてください。

## How to Maintenance and Renew

この項目は二回目以降にGitHubを使うためのフォーラムです。
ここでは、クローンしたプロジェクトを更新し自分のmasterブランチを最新のものにする方法になっています。
この作業をしないと、衝突が起こってしまうので必ずしてください。

0.初めに上記のgitの作業を行っていたディレクトリに移動します。

1.cloneは行っているはずなので省略。

2.端末に git branch -a と打ち込むと以下のような出力が出ると思います。
	* master
	  remotes/origin/HEAD -> origin/master
	  remotes/origin/master

すでに remotes/upstream/master があれば4に進んでください。


3.元のゲームプロジェクトのリポジトリをupstreamという名前で設定します。下記のコマンドを打ち込んでください。
	git remote add upstream git://github.com/SweetsGP/game.git

4.次に、git fetch upstream を打ち込んだ後、git branch -a と打ち込むと以下の出力になります。
	* master
	  remotes/origin/HEAD -> origin/master
	  remotes/origin/master
	  remotes/upstream/master (これが加わっているはず。)

5.最後に統合を行います。git merge upstream/master を打ち込んでください。
  何かしらの文字(Fast-forwardなど)が出たら更新完了です。
  何も更新がなければ Already up-to-date と出て終了です。

面倒な作業ですが、さらに面倒なことを起こさないためにもこの作業は必ず自分のプログラムの更新前に行ってください。